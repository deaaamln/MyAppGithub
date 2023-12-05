package com.dea.myappgithub.ui

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dea.myappgithub.data.local.entity.UserEntity
import com.dea.myappgithub.data.local.room.UserDao
import com.dea.myappgithub.data.local.room.UserDatabase
import com.dea.myappgithub.data.response.DetailUserResponse
import com.dea.myappgithub.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    private val _detailUser = MutableLiveData<DetailUserResponse>()

    private var userDao: UserDao?
    private var userDB: UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDao = userDB?.userDao()
    }

    fun setDetailUser(username: String) {
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    _detailUser.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Toast.makeText(getApplication(), "Request failed: " + t.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun getDetailUser(): LiveData<DetailUserResponse> = _detailUser

    fun insertUser(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserEntity(
                id,
                username,
                avatarUrl
            )
            userDao?.insertUser(user)
        }
    }

    fun checkUser(id: Int) = userDao?.checkUser(id)

    fun deleteUser(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.deleteUser(id)
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }

}