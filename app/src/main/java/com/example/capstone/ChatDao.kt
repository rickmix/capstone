package com.example.capstone

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChatDao {
    @Query("SELECT * FROM chatTable")
    fun getChats(): LiveData<List<Chat>>

    @Insert
    suspend fun addChatMessage(chat: Chat)

    @Query("DELETE FROM chatTable")
    suspend fun deleteAllChats()

    @Update
    suspend fun updateMessages(chat: Chat)

//    @Query("UPDATE chatTable SET messages = :messages WHERE phones = :phone")
//    suspend fun updateMessages(messages: MutableList<String>, phone: MutableList<String>)
}