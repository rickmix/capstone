package com.example.capstone

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun addFriend(user: User)

    @Query("UPDATE userTable SET username = :username WHERE phone = :phone")
    suspend fun updateUser(username: String, phone: String)

    @Query("SELECT * FROM userTable")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM userTable WHERE phone = :phoneNumber")
    fun getUserData(phoneNumber: String): LiveData<User>

    @Update
    suspend fun deleteUser(user: User)

    @Update
    suspend fun deleteAllUsers(user: User)
}