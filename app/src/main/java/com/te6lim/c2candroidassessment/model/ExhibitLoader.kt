package com.te6lim.c2candroidassessment.model

interface ExhibitLoader {

    suspend fun getExhibitList(): List<Exhibit>
}