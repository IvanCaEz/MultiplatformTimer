package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM session")
    fun getAllSessions(): Flow<List<Session>>

    @Upsert
    suspend fun upsertSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)
}