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

@Database(entities = [User::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserRoomDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "USER_DATABASE"

        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        fun getDatabase(context: Context): UserRoomDatabase? {
            if(INSTANCE == null) {
                synchronized(UserRoomDatabase::class.java) {
                    if(INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            UserRoomDatabase::class.java,
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