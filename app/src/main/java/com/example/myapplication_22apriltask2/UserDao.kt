package com.example.myapplication_22apriltask2

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun deleteUser(user: User)

}