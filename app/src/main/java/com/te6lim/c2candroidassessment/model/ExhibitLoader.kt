package com.te6lim.c2candroidassessment.model

import com.te6lim.c2candroidassessment.network.NetworkExhibit

interface ExhibitLoader {

    suspend fun getExhibitList(): List<NetworkExhibit>
}