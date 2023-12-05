package com.dea.myappgithub.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dea.myappgithub.R
import com.dea.myappgithub.databinding.ActivityDetailUserBinding
import com.dea.myappgithub.ui.DetailUserViewModel.Companion.EXTRA_ID
import com.dea.myappgithub.ui.DetailUserViewModel.Companion.EXTRA_URL
import com.dea.myappgithub.ui.DetailUserViewModel.Companion.EXTRA_USERNAME
import com.dea.myappgithub.ui.setting.SettingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    @SuppressLint("StringFormatInvalid", "StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        val detailViewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        if (username != null) {
            showLoading(true)
            detailViewModel.setDetailUser(username)
        }

        detailViewModel.getDetailUser().observe(this) { user ->
            user?.let {
                showLoading(false)
                val followersText = resources.getString(R.string.followers_text, it.followers)
                val followingText = resources.getString(R.string.following_text, it.following)
                with(binding) {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvLokasi.text = it.location
                    tvFollowers.text = followersText
                    tvFollowing.text = followingText
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(ivProfile)
                }
            }
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.fabFav.setImageResource(R.drawable.baseline_favorite_24)
                        _isChecked = true
                    } else {
                        binding.fabFav.setImageResource(R.drawable.baseline_favorite_border_24)
                        _isChecked = false
                    }
                }
            }
        }

        binding.fabFav.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                binding.fabFav.setImageResource(R.drawable.baseline_favorite_24)
                detailViewModel.insertUser(username.toString(), id, avatarUrl.toString())
            } else {
                binding.fabFav.setImageResource(R.drawable.baseline_favorite_border_24)
                detailViewModel.deleteUser(id)
            }
            binding.fabFav.isActivated = _isChecked
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tab.setupWithViewPager(viewPager)
        }

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

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}

