package dk.shortify.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}

object DB{
    fun get(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "history"
        )
            .allowMainThreadQueries()
            .build()
}