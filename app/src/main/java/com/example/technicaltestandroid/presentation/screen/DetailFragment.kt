package com.example.technicaltestandroid.presentation.screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.core.domain.Resource
import com.example.technicaltestandroid.R
import com.example.technicaltestandroid.databinding.FragmentDetailBinding
import com.example.technicaltestandroid.presentation.adapter.ListRepoAdapter
import com.example.technicaltestandroid.presentation.viewmodel.DetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var listRepoAdapter: ListRepoAdapter
    private val args: DetailFragmentArgs by navArgs()
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listRepoAdapter = ListRepoAdapter()

        with(binding) {
            rvListRepo.layoutManager = LinearLayoutManager(requireContext())
            rvListRepo.adapter = listRepoAdapter
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


        setDetailUserAndFav()
        setListRepo()
    }

    private fun setDetailUserAndFav() {
        detailViewModel.fetchUserDetail(args.username)
        detailViewModel.detailLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    isLoading(true)
                }

                is Resource.Success -> {
                    isLoading(false)
                    val data = resource.data
                    with(binding) {
                        tvDetailName.text = data?.name
                        tvDetailUsername.text = data?.login
                        tvDetailType.text = data?.type
                        tvCountFollowers.text = data?.followers?.toString()
                        tvCountFollowing.text = data?.following?.toString()
                        ivDetailAvatar.loadImage(data?.avatarUrl)

                        detailViewModel.isFavoriteExist(data?.login.toString())
                        detailViewModel.isFavoriteLiveData.observe(viewLifecycleOwner) {
                            val userData = detailViewModel.detailLiveData.value?.data
                            var isFavorite = it
                            setImageFav(isFavorite)
                            if (userData != null) {
                                binding.ivFavorite.setOnClickListener {
                                    if (isFavorite) {
                                        isFavorite = !isFavorite
                                        detailViewModel.deleteFavorite(userData)
                                        showNotification(
                                            requireContext(),
                                            userData.login.toString(),
                                            "Delete From Favorite"
                                        )
                                        setImageFav(isFavorite)
                                    } else {
                                        isFavorite = !isFavorite
                                        detailViewModel.insertFavorite(userData)
                                        showNotification(
                                            requireContext(),
                                            userData.login.toString(),
                                            "Add to Favorite"
                                        )
                                        setImageFav(isFavorite)
                                    }
                                }
                            }
                        }

                    }
                }

                is Resource.Error -> {
                    isLoading(false)
                    Toast.makeText(requireContext(), "Failed Get Detail Data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setListRepo() {
        detailViewModel.fetchRepoDetail(args.username)
        detailViewModel.repoLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    isLoading(true)
                }

                is Resource.Success -> {
                    isLoading(false)
                    val data = resource.data
                    if (data != null) {
                        listRepoAdapter.setData(data)
                    }
                }

                is Resource.Error -> {
                    isLoading(false)
                    Toast.makeText(
                        requireContext(),
                        "Failed Get Repository Data",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val notificationManager = ContextCompat.getSystemService(
            context, NotificationManager::class.java
        ) as NotificationManager

        val channelId = "my_channel_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun setImageFav(isFavorite: Boolean) {
        if (isFavorite) {
            binding.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivFavorite.context,
                    R.drawable.ic_favorite
                )
            )
        } else
            binding.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivFavorite.context,
                    R.drawable.ic_unfavorite
                )
            )
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .into(this)
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