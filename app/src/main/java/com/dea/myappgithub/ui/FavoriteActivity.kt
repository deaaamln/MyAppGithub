package com.dea.myappgithub.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dea.myappgithub.R
import com.dea.myappgithub.data.local.entity.UserEntity
import com.dea.myappgithub.data.response.User
import com.dea.myappgithub.databinding.ActivityFavoritBinding
import com.dea.myappgithub.ui.setting.SettingActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListUserAdapter()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserViewModel.EXTRA_USERNAME, data.login)
                intent.putExtra(DetailUserViewModel.EXTRA_ID, data.id)
                intent.putExtra(DetailUserViewModel.EXTRA_URL, data.avatarUrl)
                startActivity(intent)
            }
        })

        binding.apply {
            rvUsername.setHasFixedSize(true)
            rvUsername.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUsername.adapter = adapter
        }

        viewModel.getFavoriteUser()?.observe(this, {
            if (it != null) {
                val list = mapList(it)
                adapter.setList(list)
            }
        })

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

    }

    private fun mapList(users: List<UserEntity>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (user in users) {
            val userMapped = User(
                user.id,
                user.login,
                user.avatarUrl
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}