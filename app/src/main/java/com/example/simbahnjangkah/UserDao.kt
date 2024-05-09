package com.example.simbahnjangkah

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {

    @Upsert
    fun upsertUser(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>

    @Query("SELECT userName FROM user ORDER BY id LIMIT 1")
    fun getUserName(): String

    @Query("SELECT userAge FROM user ORDER BY id LIMIT 1")
    fun getUserAge(): Int
    @Query("SELECT userGender FROM user ORDER BY id LIMIT 1")
    fun getUserGender(): String

    @Query("SELECT COUNT(*) FROM user")
    suspend fun getUserCount(): Int

}