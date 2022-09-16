package com.te6lim.c2candroidassessment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.te6lim.c2candroidassessment.database.DBExhibit
import com.te6lim.c2candroidassessment.database.ExhibitDatabase
import com.te6lim.c2candroidassessment.network.NetworkExhibit
import com.te6lim.c2candroidassessment.model.RestExhibitLoader
import kotlinx.coroutines.*

class ExhibitRepository(
    private val exhibitLoader: RestExhibitLoader, private val exhibitDatabase: ExhibitDatabase
) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private val dbExhibits = exhibitDatabase.exhibitDao.getAll()

    private val networkData = MutableLiveData<List<NetworkExhibit>>()

    val exhibitList = fetchList(dbExhibits, networkData)

    private fun fetchList(
        dbData: LiveData<List<DBExhibit>?>, networkData: MutableLiveData<List<NetworkExhibit>>
    ): LiveData<List<DBExhibit>> {
        val result = MediatorLiveData<List<DBExhibit>>()

        val dbObserver = Observer<List<DBExhibit>?> {
            it?.let {
                result.value = it
            } ?: run {
                scope.launch {
                    networkData.postValue(exhibitLoader.getExhibitList())
                }
            }
        }

        val networkObserver = Observer<List<NetworkExhibit>?> {
            scope.launch {
                exhibitDatabase.exhibitDao.addAll(it.toDatabaseExhibitList())
            }
        }

        result.addSource(dbData, dbObserver)
        result.addSource(networkData, networkObserver)
        return result
    }

    fun refreshList() {
        scope.launch {
            withContext(Dispatchers.IO) {
                exhibitDatabase.exhibitDao.clear()
                networkData.value = exhibitLoader.getExhibitList()
            }
        }

    }

}

private fun List<NetworkExhibit>.toDatabaseExhibitList(): List<DBExhibit> {
    return this.map {
        DBExhibit(title = it.title, images = it.images)
    }
}
