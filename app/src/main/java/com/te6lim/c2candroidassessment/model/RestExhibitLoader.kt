package com.te6lim.c2candroidassessment.model

import com.te6lim.c2candroidassessment.network.ExhibitApi
import com.te6lim.c2candroidassessment.network.NetworkExhibit
import java.lang.Exception

class RestExhibitLoader(
    private val network: ExhibitApi, private val networkStateListener: NetworkStateListener
) : ExhibitLoader {

    enum class NetworkState {
        DONE, LOADING, ERROR
    }

    override suspend fun getExhibitList(): List<NetworkExhibit> {
        return try {
            networkStateListener.onStateResolved(NetworkState.LOADING)
            val response = network.retrofitService.getExhibitsAsync().await()
            networkStateListener.onStateResolved(NetworkState.DONE)
            response
        } catch (e: Exception) {
            networkStateListener.onStateResolved(NetworkState.ERROR)
            listOf()
        }
    }

    interface NetworkStateListener {

        fun onStateResolved(state: NetworkState)
    }
}