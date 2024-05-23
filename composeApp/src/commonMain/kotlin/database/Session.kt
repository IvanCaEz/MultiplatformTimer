package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionName: String,
    val intervals: Int,
    val warmupTime: Int? = 0,
    val workTime: Int,
    val restTime: Int,
    val cooldownTime: Int? = 0
)