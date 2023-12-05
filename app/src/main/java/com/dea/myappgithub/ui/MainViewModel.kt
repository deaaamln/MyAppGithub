package com.dea.myappgithub.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dea.myappgithub.data.response.User
import com.dea.myappgithub.data.response.UserResponse
import com.dea.myappgithub.data.retrofit.ApiConfig
import com.dea.myappgithub.ui.setting.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    private val listUser = MutableLiveData<ArrayList<User>>()

    fun setSearchUser(query: String) {
        val call = ApiConfig.getApiService().getSearchUser(query)
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    listUser.postValue(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.d("Failure", it) }
            }
        })
    }

    fun getSearchUser(): LiveData<ArrayList<User>> = listUser

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}