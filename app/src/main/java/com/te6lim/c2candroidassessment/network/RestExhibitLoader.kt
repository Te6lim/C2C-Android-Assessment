package com.te6lim.c2candroidassessment.network

import com.te6lim.c2candroidassessment.model.Exhibit
import com.te6lim.c2candroidassessment.repository.*
import java.lang.Exception

class RestExhibitLoader(
    private val network: ExhibitApi, private val loadStateListener: LoadStateListener
) : ExhibitLoader {

    var isRefreshing = false

    override suspend fun getExhibitList(): List<Exhibit> {
        return try {
            loadStateListener.onStateResolved(LoadState.LOADING, LoadSource.NETWORK)
            val response = network.retrofitService.getExhibitsAsync().await()
            loadStateListener.onStateResolved(LoadState.DONE, LoadSource.NETWORK)
            if (isRefreshing) loadStateListener.onRefresh(true)
            response
        } catch (e: Exception) {
            loadStateListener.onStateResolved(LoadState.ERROR, LoadSource.NETWORK)
            if (isRefreshing) loadStateListener.onRefresh(false)
            listOf()
        }
    }
}