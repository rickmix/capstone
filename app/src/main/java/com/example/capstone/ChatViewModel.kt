package com.example.capstone

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ChatViewModel(application: Application): AndroidViewModel(application) {

    private val chatRepository = ChatRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    //    val game = gameRepository.getGames()
//    val error = MutableLiveData<String>()
//    val successMessage = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    val chats: LiveData<List<Chat>> = chatRepository.getAllChats()

//    fun getChatData(chatId: Long?): LiveData<Chat> = chatRepository.getChatMessages(chatId)

    fun insertMessage(message: String, chatData: Chat) {

        chatData

        var newChatList: MutableList<String> = chatData.messages

        newChatList.add(message)

        var newChat = Chat(
            messages = newChatList,
            phones = chatData.phones,
            id = chatData.id
        )

        mainScope.launch {
            withContext(Dispatchers.IO) {
                chatRepository.updateMessages(newChat)
            }
            success.value = true
        }
    }

    fun newUser(message: MutableList<String>, phones: MutableList<String>) {
        val newChat = Chat(
            messages = message,
            phones = phones
        )

        mainScope.launch {
            withContext(Dispatchers.IO) {
                chatRepository.insertChatMessage(newChat)
            }

            success.value = true
        }
    }

    fun deleteAllChats() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                chatRepository.deleteAllChats()
            }
        }
    }
}