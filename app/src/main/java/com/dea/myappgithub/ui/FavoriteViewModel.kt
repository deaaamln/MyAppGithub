package com.dea.myappgithub.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dea.myappgithub.data.local.entity.UserEntity
import com.dea.myappgithub.data.local.room.UserDao
import com.dea.myappgithub.data.local.room.UserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var userDao: UserDao?
    private var userDB: UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDao = userDB?.userDao()
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>>? {
        return userDao?.getFavorite()
    }
}