package com.dea.myappgithub.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dea.myappgithub.R
import com.dea.myappgithub.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _binding: FragmentFollowersBinding? = null
    private val binding: FragmentFollowersBinding
        get() {
            return _binding ?: throw IllegalArgumentException("Binding has not initialize")
        }
    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: ListUserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUserViewModel.EXTRA_USERNAME).toString()
        _binding = FragmentFollowersBinding.bind((view))

        adapter = ListUserAdapter()

        binding.apply {
            rvUsername.setHasFixedSize(true)
            rvUsername.layoutManager = LinearLayoutManager(activity)
            rvUsername.adapter = adapter
        }
        showLoading(true)
        viewModel = ViewModelProvider(this)[FollowersViewModel::class.java]
        viewModel.setListFollowers(username)
        viewModel.getListFollowers().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}