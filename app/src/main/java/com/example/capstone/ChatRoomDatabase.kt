package com.example.capstone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [Chat::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ChatRoomDatabase: RoomDatabase() {
    abstract fun chatDao(): ChatDao

    companion object {
        private const val DATABASE_NAME = "CHAT_DATABASE"

        @Volatile
        private var INSTANCE: ChatRoomDatabase? = null

        fun getDatabase(context: Context): ChatRoomDatabase? {
            if(INSTANCE == null) {
                synchronized(ChatRoomDatabase::class.java) {
                    if(INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ChatRoomDatabase::class.java,
                            DATABASE_NAME
                        )
                            .fallbackToDestructiveMigration()
//                            .addCallback(object: RoomDatabase.Callback() {
//                                override fun onCreate(db: SupportSQLiteDatabase) {
//                                    super.onCreate(db)
//                                    INSTANCE?.let { database -> CoroutineScope(Dispatchers.IO).launch {
//                                        database.noteDao().insertNote((Note("Title", Date(), "")))
//                                    } }
//                                }
//                            })
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}