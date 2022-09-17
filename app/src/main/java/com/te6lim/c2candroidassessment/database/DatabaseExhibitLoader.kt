package com.te6lim.c2candroidassessment.database

import com.te6lim.c2candroidassessment.model.Exhibit
import com.te6lim.c2candroidassessment.repository.*

class DatabaseExhibitLoader(
    private val database: ExhibitDatabase, private val loadStateListener: LoadStateListener
) : ExhibitLoader {

    override suspend fun getExhibitList(): List<Exhibit> {
        loadStateListener.onStateResolved(LoadState.LOADING, LoadSource.DATABASE)
        val data = database.exhibitDao.getAll()
        if (data.isNotEmpty()) loadStateListener.onStateResolved(LoadState.DONE, LoadSource.DATABASE)
        else loadStateListener.onStateResolved(LoadState.ERROR, LoadSource.DATABASE)
        return data
    }

    suspend fun clearExhibitList() {
        database.exhibitDao.clear()
    }

    suspend fun addAllExhibits(list: List<DBExhibit>) {
        database.exhibitDao.addAll(list)
    }
}
