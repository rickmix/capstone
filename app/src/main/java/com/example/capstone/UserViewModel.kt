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

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val userRepository = UserRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    //    val game = gameRepository.getGames()
    val error = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    val users: LiveData<List<User>> = userRepository.getAllUsers()

    fun getUserData(phone: String) = userRepository.getUserData(phone)

    fun newUser(userName: String, phone: String, img: String) {
        val newUser = User(
            username = userName,
            phone = phone,
            img = img,
            friends = ArrayList()
        )

        mainScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.insertNewUser(newUser)
            }

            success.value = true
        }
    }

    fun checkIfExists(friendId: String, userData: User): Boolean {
        var friendAlreadyExists = false

        for(i in userData.friends) {
            if(i == friendId) {
                friendAlreadyExists = true
            }
        }

        return friendAlreadyExists
    }

    fun addFriend(friendId: String, userData: User) {
        if(checkIfExists(friendId, userData)) {
            error.value = "Friend already exists!"
        } else {
            val list: MutableList<String> = userData.friends
            list.add(friendId)

            val newFriend = User(
                id = userData.id,
                username = userData.username,
                phone = userData.phone,
                img = userData.img,
                friends = list
            )

            mainScope.launch {
                withContext(Dispatchers.IO) {
                    userRepository.addFriend(newFriend)
                }
            }

            successMessage.value = "Friend Added!"
        }
    }

    fun deleteUser(user: User) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.deleteUser(user)
            }
        }
    }

    fun deleteAllContacts() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.deleteAllUsers()
            }
        }
    }

    fun editUserDetails(username: String, phone: String) {
        if(username.isNotBlank() || phone.isNotBlank()) {
            success.value = true
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    userRepository.updateUser(username, phone)
                }
            }
        } else {
            error.value = "Username can not be empty"
        }
    }

//    private fun isNoteValid(note: Note): Boolean {
//        return when {
//            note.title.isBlank() -> {
//                error.value = "Title must no be empty"
//                false
//            }
//            else -> true
//        }
//    }
}