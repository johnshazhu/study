package com.drcuiyutao.lib.db.util

import android.content.Context
import androidx.room.Room
import com.drcuiyutao.lib.db.AppDatabase

object DatabaseUtil {
    fun getDatabase(context: Context): AppDatabase? {
        return AppDatabase.getInstance(context)
    }
}
