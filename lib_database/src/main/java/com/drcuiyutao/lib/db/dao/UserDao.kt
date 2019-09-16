package com.drcuiyutao.lib.db.dao

import androidx.room.*
import com.drcuiyutao.lib.db.entity.User
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flowable<List<User>>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User): Completable

    @Delete
    fun delete(user: User)

    @Update
    fun updateUsers(vararg users: User)

    @Query("SELECT * FROM user WHERE userid = :userId")
    fun getUserById(userId: String): Flowable<User>
}