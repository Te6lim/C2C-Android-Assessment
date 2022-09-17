package com.te6lim.c2candroidassessment.repository

import androidx.lifecycle.*
import com.te6lim.c2candroidassessment.database.DatabaseExhibitLoader
import com.te6lim.c2candroidassessment.model.Exhibit
import com.te6lim.c2candroidassessment.network.RestExhibitLoader
import com.te6lim.c2candroidassessment.toDBExhibitList
import kotlinx.coroutines.*

class ExhibitRepository(
    private val networkLoader: RestExhibitLoader, private val databaseLoader: DatabaseExhibitLoader
) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val dbExhibits = MutableLiveData<List<Exhibit>>().apply {
        scope.launch { postValue(databaseLoader.getExhibitList()) }
    }

    private val networkData = MutableLiveData<List<Exhibit>>()

    val exhibitList = fetchList(dbExhibits, networkData)

    private var initialLoad = true

    private fun fetchList(
        dbData: MutableLiveData<List<Exhibit>>, networkData: MutableLiveData<List<Exhibit>>
    ): LiveData<List<Exhibit>> {

        val result = MediatorLiveData<List<Exhibit>>()

        result.addSource(dbData, getDatabaseObserver(networkData, result))
        result.addSource(networkData, getNetworkObserver(dbData, result))
        return result
    }

    private fun getDatabaseObserver(
        networkData: MutableLiveData<List<Exhibit>>, result: MediatorLiveData<List<Exhibit>>
    ) = Observer<List<Exhibit>> {
        if (it.isNotEmpty()) {
            if (initialLoad) postNetworkLiveData(networkData)
            else result.value = it
        } else postNetworkLiveData(networkData)
    }

    private fun getNetworkObserver(
        dbData: MutableLiveData<List<Exhibit>>, result: MediatorLiveData<List<Exhibit>>
    ) = Observer<List<Exhibit>> {
        scope.launch {
            initialLoad = false
            if (it.isNotEmpty()) {
                databaseLoader.clearExhibitList()
                databaseLoader.addAllExhibits(it.toDBExhibitList())
                result.value = it

            } else result.value = databaseLoader.getExhibitList()
        }
    }

    private fun postNetworkLiveData(networkData: MutableLiveData<List<Exhibit>>) {
        scope.launch {
            if (initialLoad) networkLoader.isRefreshing = true
            val list = networkLoader.getExhibitList()
            networkData.postValue(list)
        }
    }

    fun refreshList() {
        scope.launch {
            databaseLoader.clearExhibitList()
            networkLoader.isRefreshing = true
            val list = networkLoader.getExhibitList()
            networkData.value = list
        }
    }

}
