package com.dea.myappgithub.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dea.myappgithub.R
import com.dea.myappgithub.data.response.User
import com.dea.myappgithub.databinding.ActivityMainBinding
import com.dea.myappgithub.ui.setting.SettingActivity
import com.dea.myappgithub.ui.setting.SettingPreferences

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var mainViewModel: MainViewModel
    private var list = ArrayList<User>()

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(pref)).get(
            MainViewModel::class.java
        )
        getThemeSetting()

        adapter = ListUserAdapter()

        binding.rvUsername.layoutManager = LinearLayoutManager(this)
        binding.rvUsername.setHasFixedSize(true)
        binding.rvUsername.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserViewModel.EXTRA_USERNAME, data.login)
                intent.putExtra(DetailUserViewModel.EXTRA_ID, data.id)
                intent.putExtra(DetailUserViewModel.EXTRA_URL, data.avatarUrl)
                startActivity(intent)
            }
        })

        binding.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() == true) {
                    searchUser(newText)
                    showLoading(true)
                    binding.tvResult.text = getString(R.string.searching_status_info)
                } else {
                    showLoading(false)
                    list.clear()
                    binding.tvResult.text = getString(R.string.instruction_status_info)
                }
                return true
            }
        })

        observeSearchUser()

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorit -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }


    private fun getThemeSetting() {
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun searchUser(query: String) {
        showLoading(true)
        mainViewModel.setSearchUser(query)
    }

    private fun observeSearchUser() {
        mainViewModel.getSearchUser().observe(this) { userList ->
            showLoading(false)
            if (userList != null) {
                adapter.setList(userList)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}