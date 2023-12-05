package com.dea.myappgithub.ui

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dea.myappgithub.data.response.User
import com.dea.myappgithub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel(application: Application) : AndroidViewModel(application) {
    val listFollowing = MutableLiveData<ArrayList<User>>()

    fun setListFollowing(username: String) {
        val call = ApiConfig.getApiService()
            .getFollowing(username)
        call.enqueue(@SuppressLint("SuspiciousIndentation")
        object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    listFollowing.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(
                    getApplication(),
                    "Request failed: " + t.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        })
    }

    fun getListFollowing(): LiveData<ArrayList<User>> = listFollowing

}
