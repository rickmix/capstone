package com.example.capstone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.navigation.fragment.findNavController

class SessionManager {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var con: Context

    constructor(con: Context) {
        this.con = con
        pref = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    companion object {
        val SHARED_PREF_NAME: String = "session"
        val IS_LOGIN: String = "isLoggedIn"
        val PHONE: String = "phone"
        val USER_NAME: String = "userName"
    }

    fun getUserPhone(): String{
        return pref.getString(PHONE, null).toString()
    }

    fun getUserName(): String{
        return pref.getString(USER_NAME, null).toString()
    }

    fun createLoginSession(phone: String, name: String) {
        editor.putString(PHONE, phone)
        editor.putString(USER_NAME, name)
        editor.putBoolean(IS_LOGIN, true)
        editor.commit()
    }

    fun editUserDetails(username: String) {
        editor.putString(USER_NAME, username)
        editor.commit()
    }

    fun logOutUser() {
        editor.clear()
        editor.commit()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

}