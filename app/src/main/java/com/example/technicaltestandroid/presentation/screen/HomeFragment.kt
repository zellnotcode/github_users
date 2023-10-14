package com.example.technicaltestandroid.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.technicaltestandroid.databinding.FragmentHomeBinding
import com.example.technicaltestandroid.presentation.adapter.ListUserAdapter
import com.example.technicaltestandroid.presentation.adapter.UserLoadingStateAdapter
import com.example.technicaltestandroid.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val listUserAdapter: ListUserAdapter by lazy {
        ListUserAdapter { login ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(login)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvListUser.layoutManager = LinearLayoutManager(requireContext())
            rvListUser.adapter = listUserAdapter.withLoadStateFooter(
                UserLoadingStateAdapter()
            )
        }

        listUserAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is LoadState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed Get User Data", Toast.LENGTH_SHORT)
                        .show()
                }
                is LoadState.NotLoading -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        binding.ivMoveFavorite.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
        }

        setListUser()
        setSearchUser()
    }

    private fun setListUser() {
        homeViewModel.listUser.observe(viewLifecycleOwner) { pagingData ->
            listUserAdapter.submitData(lifecycle, pagingData)
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setSearchUser() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    setListUser()
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    homeViewModel.searchUsers(query).observe(viewLifecycleOwner) { listUser ->
                        listUserAdapter.submitData(lifecycle, listUser)
                    }
                }
                return false
            }
        })
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}