package database
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory
fun getSessionDatabase(): SessionDatabase {
    val dbFile = NSHomeDirectory() + "/session.db"
    return Room.databaseBuilder<SessionDatabase>(
        name = dbFile,
        factory = { SessionDatabase::class.instantiateImpl() })
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(false)
        .build()
}