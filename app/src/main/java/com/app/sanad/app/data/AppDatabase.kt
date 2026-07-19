package com.app.sanad.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.sanad.getLibraryContent.data.LibraryContent
import com.app.sanad.getLibraryContent.data.LibraryDao
import com.app.sanad.users.patient.calender.data.daos.DayDao
import com.app.sanad.users.patient.calender.data.daos.TaskDao
import com.app.sanad.users.patient.calender.data.entity.DayEntity
import com.app.sanad.users.patient.calender.data.entity.TaskEntity
import com.app.sanad.users.patient.dailyprogram.data.daos.DayTaskDao
import com.app.sanad.users.patient.dailyprogram.data.entity.DayTaskEntity
import com.app.sanad.users.patient.dailyprogram.data.entity.TaskListConverter

@Database(
    entities = [LibraryContent::class , DayEntity::class, TaskEntity::class, DayTaskEntity::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(TaskListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun libraryDao(): LibraryDao
    abstract fun dayDao(): DayDao
    abstract fun taskDao(): TaskDao
    abstract fun dayTaskDao(): DayTaskDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-name"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}