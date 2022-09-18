package com.te6lim.c2candroidassessment.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.te6lim.c2candroidassessment.model.Exhibit

@Database(entities = [DBExhibit::class], version = 1, exportSchema = true)
@TypeConverters(ImageListConverter::class)
abstract class ExhibitDatabase : RoomDatabase() {

    abstract val exhibitDao: ExhibitDao

    companion object {
        private var INSTANCE: ExhibitDatabase? = null

        fun getInstance(context: Context): ExhibitDatabase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(context, ExhibitDatabase::class.java, "exhibitDatabase")
                    .addTypeConverter(ImageListConverter())
                    .build()
                INSTANCE = instance
            }
            return instance
        }
    }
}