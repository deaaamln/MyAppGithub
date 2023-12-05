package com.dea.myappgithub.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dea.myappgithub.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM favorite_users")
    fun getFavorite(): LiveData<List<UserEntity>>

    @Query("SELECT count(*) FROM favorite_users WHERE favorite_users.id = :id")
    fun checkUser(id: Int): Int

    @Query("DELETE FROM favorite_users WHERE favorite_users.id = :id")
    fun deleteUser(id: Int): Int
}