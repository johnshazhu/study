package com.drcuiyutao.lib.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.drcuiyutao.lib.db.dao.UserDao
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase
}