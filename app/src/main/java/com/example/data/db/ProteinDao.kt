package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProteinDao {
    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // Mistakes
    @Query("SELECT * FROM mistakes WHERE isResolved = 0 ORDER BY timestamp DESC")
    fun getUnresolvedMistakes(): Flow<List<MistakeEntity>>

    @Query("SELECT * FROM mistakes ORDER BY timestamp DESC")
    fun getAllMistakes(): Flow<List<MistakeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: MistakeEntity): Long

    @Update
    suspend fun updateMistake(mistake: MistakeEntity)

    @Query("UPDATE mistakes SET isResolved = 1 WHERE id = :id")
    suspend fun markMistakeResolved(id: Long)

    @Query("DELETE FROM mistakes")
    suspend fun clearAllMistakes()

    @Query("DELETE FROM user_profile")
    suspend fun clearUserProfile()
}
