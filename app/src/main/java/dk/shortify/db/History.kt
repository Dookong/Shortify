package dk.shortify.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    val type: Int,
    val origin: String,
    val shorten: String
){
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
}