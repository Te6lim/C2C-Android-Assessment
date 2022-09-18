package com.te6lim.c2candroidassessment.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExhibitDao {

    @Query("SELECT * FROM exhibit")
    suspend fun getAll(): List<DBExhibit>

    @Insert
    suspend fun addAll(list: List<DBExhibit>): List<Long>

    @Query("DELETE FROM exhibit")
    suspend fun clear()
}