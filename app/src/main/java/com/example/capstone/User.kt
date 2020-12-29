package com.example.capstone

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userTable")
data class User(
    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "phone")
    var phone: String,

    @ColumnInfo(name = "img")
    var img: String,

    @ColumnInfo(name = "friends")
    var friends: MutableList<String>,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)