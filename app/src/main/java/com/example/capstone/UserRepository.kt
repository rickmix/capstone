package com.example.capstone

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UserRepository (context: Context) {
    private val userDao: UserDao

    init {
        val database = UserRoomDatabase.getDatabase(context)
        userDao = database!!.userDao()
    }

    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers() ?: MutableLiveData(emptyList())
    }

    fun getUserData(phone: String): LiveData<User> {
        return userDao.getUserData(phone)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user);
    }

    suspend fun deleteAllUsers(user: User) {
        userDao.deleteAllUsers(user)
    }

    suspend fun insertNewUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun addFriend(user: User) {
        userDao.addFriend(user)
    }

    suspend fun updateUser(username: String, phone: String) {
        userDao.updateUser(username, phone)
    }
}