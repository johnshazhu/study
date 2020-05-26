package com.study.lib.db.util

import android.content.Context
import com.study.lib.db.AppDatabase

object DatabaseUtil {
    fun getDatabase(context: Context): AppDatabase? {
        return AppDatabase.getInstance(context)
    }
}
