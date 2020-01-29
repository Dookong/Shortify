package dk.shortify.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): LiveData<List<History>>

    @Insert
    fun insert(history: History)

    @Delete
    fun delete(history: History)

    @Query("DELETE FROM history")
    fun clear()
}