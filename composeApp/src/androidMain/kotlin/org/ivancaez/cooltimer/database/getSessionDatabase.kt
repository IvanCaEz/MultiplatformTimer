package org.ivancaez.cooltimer.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.SessionDatabase

fun getSessionDatabase(context: Context): SessionDatabase {
    val dbFile = context.getDatabasePath("session.db")
    return Room.databaseBuilder(context, SessionDatabase::class.java, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(false)
        .build()
}