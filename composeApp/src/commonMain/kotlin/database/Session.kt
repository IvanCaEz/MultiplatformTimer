package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var sessionName: String,
    var intervals: Int,
    var warmupTime: Int? = 0,
    var workTime: Int,
    var restTime: Int,
    var cooldownTime: Int? = 0
)