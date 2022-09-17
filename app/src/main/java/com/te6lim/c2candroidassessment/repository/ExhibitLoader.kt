package com.te6lim.c2candroidassessment.repository

import com.te6lim.c2candroidassessment.model.Exhibit


interface ExhibitLoader {

    suspend fun getExhibitList(): List<Exhibit>
}