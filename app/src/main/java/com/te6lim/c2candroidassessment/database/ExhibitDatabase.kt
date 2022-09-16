package com.te6lim.c2candroidassessment.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Database(entities = [Exhibit::class], version = 1, exportSchema = true)
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

@Dao
interface ExhibitDao {

    @Query("SELECT * FROM exhibit")
    fun getAll(): LiveData<List<Exhibit>>

    @Insert
    fun addAll(list: List<Exhibit>): List<Long>

    @Query("DELETE FROM exhibit")
    fun clear()
}

@Entity(tableName = "exhibit")
data class Exhibit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo val title: String? = null,
    @ColumnInfo val images: List<String>? = null
)