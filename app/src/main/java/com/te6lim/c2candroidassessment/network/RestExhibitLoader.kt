package com.te6lim.c2candroidassessment.network

import com.te6lim.c2candroidassessment.model.Exhibit
import com.te6lim.c2candroidassessment.repository.ExhibitLoader
import com.te6lim.c2candroidassessment.repository.LoadState
import com.te6lim.c2candroidassessment.repository.LoadStateListener
import java.lang.Exception

class RestExhibitLoader(
    private val network: ExhibitApi, private val loadStateListener: LoadStateListener
) : ExhibitLoader {

    var isRefreshing = false

    override suspend fun getExhibitList(): List<Exhibit> {
        return try {
            loadStateListener.onStateResolved(LoadState.LOADING)
            val response = network.retrofitService.getExhibitsAsync().await()
            loadStateListener.onStateResolved(LoadState.DONE)
            if (isRefreshing) loadStateListener.onRefresh(true)
            response
        } catch (e: Exception) {
            loadStateListener.onStateResolved(LoadState.ERROR)
            if (isRefreshing) loadStateListener.onRefresh(false)
            listOf()
        }
    }
}