package com.te6lim.c2candroidassessment.model

import com.te6lim.c2candroidassessment.network.ExhibitApiService
import java.lang.Exception

class RestExhibitLoader(private val network: ExhibitApiService) : ExhibitLoader {

    override suspend fun getExhibitList(): List<Exhibit> {
        return try {
            network.getExhibitsAsync().await()
        } catch (e: Exception) {
            listOf()
        }
    }
}