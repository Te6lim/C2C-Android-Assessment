package com.te6lim.c2candroidassessment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.te6lim.c2candroidassessment.model.Exhibit

@Entity(tableName = "exhibit")
data class DBExhibit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo override val title: String? = null,
    @ColumnInfo override val images: List<String>? = null
) : Exhibit(title, images)