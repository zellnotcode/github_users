package com.example.technicaltestandroid.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.domain.Resource
import com.example.technicaltestandroid.databinding.FragmentFavoriteBinding
import com.example.technicaltestandroid.presentation.adapter.ListFavoriteAdapter
import com.example.technicaltestandroid.presentation.viewmodel.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private val listFavoriteAdapter: ListFavoriteAdapter by lazy {
        ListFavoriteAdapter { login ->
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(login)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvListFavorite.layoutManager = LinearLayoutManager(requireContext())
            rvListFavorite.adapter = listFavoriteAdapter
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


        setFavoriteList()
    }

    private fun setFavoriteList() {
        favoriteViewModel.listUserFavorite.observe(viewLifecycleOwner) {resource ->
            when (resource) {
                is Resource.Loading -> {
                    isLoading(true)
                }

                is Resource.Success -> {
                    isLoading(false)
                    val data = resource.data
                    if (data != null) {
                        listFavoriteAdapter.setData(data)
                    }
                }

                is Resource.Error -> {
                    isLoading(false)
                    Toast.makeText(requireContext(), "Failed Get Favorite Data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
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