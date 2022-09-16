package com.te6lim.c2candroidassessment.repository

import androidx.lifecycle.*
import com.te6lim.c2candroidassessment.database.DBExhibit
import com.te6lim.c2candroidassessment.database.ExhibitDatabase
import com.te6lim.c2candroidassessment.network.NetworkExhibit
import com.te6lim.c2candroidassessment.network.ExhibitApi
import kotlinx.coroutines.*
import java.lang.Exception

class ExhibitRepository(
    private val network: ExhibitApi, private val exhibitDatabase: ExhibitDatabase,
    private val loadStateListener: LoadStateListener
) : ExhibitLoader {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val dbExhibits = MutableLiveData<List<DBExhibit>>().apply {
        scope.launch { postValue(exhibitDatabase.exhibitDao.getAll()) }
    }

    private val networkData = MutableLiveData<List<NetworkExhibit>>()

    val exhibitList = fetchList(dbExhibits, networkData)

    private var initialLoad = true

    enum class NetworkState {
        DONE, LOADING, ERROR
    }

    override suspend fun getExhibitList(): List<NetworkExhibit> {
        return try {
            loadStateListener.onStateResolved(NetworkState.LOADING)
            val response = network.retrofitService.getExhibitsAsync().await()
            loadStateListener.onStateResolved(NetworkState.DONE)
            response
        } catch (e: Exception) {
            loadStateListener.onStateResolved(NetworkState.ERROR)
            listOf()
        }
    }

    private fun fetchList(
        dbData: MutableLiveData<List<DBExhibit>>, networkData: MutableLiveData<List<NetworkExhibit>>
    ): LiveData<List<DBExhibit>> {
        val result = MediatorLiveData<List<DBExhibit>>()

        val dbObserver = Observer<List<DBExhibit>> {
            if (it.isNotEmpty()) {
                if (initialLoad) {
                    postNetworkData(networkData)
                } else {
                    loadStateListener.onStateResolved(NetworkState.DONE)
                    result.value = it
                }
            } else {
                postNetworkData(networkData)
            }
        }

        val networkObserver = Observer<List<NetworkExhibit>> {
            scope.launch {
                initialLoad = false
                if (it.isNotEmpty()) {
                    exhibitDatabase.exhibitDao.clear()
                    exhibitDatabase.exhibitDao.addAll(it.toDatabaseExhibitList())
                    dbData.postValue(exhibitDatabase.exhibitDao.getAll())

                } else {
                    if (dbData.value!!.isNotEmpty()) loadStateListener.onStateResolved(NetworkState.DONE)
                    else loadStateListener.onStateResolved(NetworkState.ERROR)
                    result.value = dbData.value
                }
            }
        }

        result.addSource(dbData, dbObserver)
        result.addSource(networkData, networkObserver)
        return result
    }

    private fun postNetworkData(networkData: MutableLiveData<List<NetworkExhibit>>) {
        scope.launch {
            val list = getExhibitList()
            networkData.postValue(list)
        }
    }

    fun refreshList() {
        scope.launch {
            exhibitDatabase.exhibitDao.clear()
            val list = getExhibitList()
            networkData.value = list
        }

    }

}

private fun List<NetworkExhibit>.toDatabaseExhibitList(): List<DBExhibit> {
    return this.map {
        DBExhibit(title = it.title, images = it.images)
    }
}
