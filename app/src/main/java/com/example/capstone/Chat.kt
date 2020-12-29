package com.example.capstone

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatTable")
data class Chat(
    @ColumnInfo(name = "phones")
    var phones: MutableList<String>,

    @ColumnInfo(name = "messages")
    var messages: MutableList<String>,


    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)