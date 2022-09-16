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

    private val dbExhibits = exhibitDatabase.exhibitDao.getAll()

    private val networkData = MutableLiveData<List<NetworkExhibit>>()

    val exhibitList = fetchList(dbExhibits, networkData)

    enum class NetworkState {
        DONE, LOADING, ERROR
    }

    override suspend fun getExhibitList(): List<NetworkExhibit> {
        return try {
            loadStateListener.onStateResolved(NetworkState.LOADING)
            val response = getNetworkExhibitList()
            loadStateListener.onStateResolved(NetworkState.DONE)
            response
        } catch (e: Exception) {
            loadStateListener.onStateResolved(NetworkState.ERROR)
            listOf()
        }
    }

    private fun fetchList(
        dbData: LiveData<List<DBExhibit>>, networkData: MutableLiveData<List<NetworkExhibit>>
    ): LiveData<List<DBExhibit>> {
        val result = MediatorLiveData<List<DBExhibit>>()

        val dbObserver = Observer<List<DBExhibit>?> {
            if (it.isNotEmpty()) {
                loadStateListener.onStateResolved(NetworkState.DONE)
                result.value = it
            } else {
                scope.launch {
                    networkData.postValue(getNetworkExhibitList())
                }
            }
        }

        val networkObserver = Observer<List<NetworkExhibit>> {
            scope.launch {
                if (it.isNotEmpty()) {
                    exhibitDatabase.exhibitDao.addAll(it.toDatabaseExhibitList())
                }
            }
        }

        result.addSource(dbData, dbObserver)
        result.addSource(networkData, networkObserver)
        return result
    }

    private suspend fun getNetworkExhibitList(): List<NetworkExhibit> {
        return network.retrofitService.getExhibitsAsync().await()
    }

    fun refreshList() {
        scope.launch {
            withContext(Dispatchers.IO) {
                exhibitDatabase.exhibitDao.clear()
                networkData.value = getNetworkExhibitList()
            }
        }

    }

}

private fun List<NetworkExhibit>.toDatabaseExhibitList(): List<DBExhibit> {
    return this.map {
        DBExhibit(title = it.title, images = it.images)
    }
}
