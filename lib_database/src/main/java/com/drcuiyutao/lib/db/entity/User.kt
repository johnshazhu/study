package com.drcuiyutao.lib.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
data class User(@PrimaryKey
                @ColumnInfo(name = "userid")
                var uid: String,
                @ColumnInfo(name = "first_name")
                val firstName: String,
                @ColumnInfo(name = "last_name")
                val lastName: String,
                val age: Int)