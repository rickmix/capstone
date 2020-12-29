package com.example.capstone

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ChatRepository (context: Context) {
    private val chatDao: ChatDao

    init {
        val database = ChatRoomDatabase.getDatabase(context)
        chatDao = database!!.chatDao()
    }

    fun getAllChats(): LiveData<List<Chat>> {
        return chatDao.getChats() ?: MutableLiveData(emptyList())
    }

//    fun getChatMessages(chatId: Long?): LiveData<Chat> {
//        return chatDao.getChatMessages()?: MutableLiveData(emptyList())
//    }

    suspend fun updateMessages(chat: Chat) {
        chatDao.updateMessages(chat)
    }

    suspend fun insertChatMessage(chat: Chat) {
        chatDao.addChatMessage(chat)
    }

    suspend fun deleteAllChats() {
        chatDao.deleteAllChats()
    }
}