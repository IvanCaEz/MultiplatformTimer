package database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Session::class], version = 2)
abstract class SessionDatabase: RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}