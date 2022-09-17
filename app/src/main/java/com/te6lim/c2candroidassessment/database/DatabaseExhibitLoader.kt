package com.te6lim.c2candroidassessment.database

import com.te6lim.c2candroidassessment.model.Exhibit
import com.te6lim.c2candroidassessment.repository.ExhibitLoader
import com.te6lim.c2candroidassessment.repository.LoadState
import com.te6lim.c2candroidassessment.repository.LoadStateListener

class DatabaseExhibitLoader(
    private val database: ExhibitDatabase, private val loadStateListener: LoadStateListener
) : ExhibitLoader {

    override suspend fun getExhibitList(): List<Exhibit> {
        loadStateListener.onStateResolved(LoadState.LOADING)
        val data = database.exhibitDao.getAll()
        if (data.isNotEmpty()) loadStateListener.onStateResolved(LoadState.DONE)
        else loadStateListener.onStateResolved(LoadState.ERROR)
        return data
    }

    suspend fun clearExhibitList() {
        database.exhibitDao.clear()
    }

    suspend fun addAllExhibits(list: List<DBExhibit>) {
        database.exhibitDao.addAll(list)
    }
}
