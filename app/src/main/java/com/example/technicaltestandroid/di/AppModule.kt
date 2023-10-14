package com.example.technicaltestandroid.di

import com.example.technicaltestandroid.presentation.viewmodel.DetailViewModel
import com.example.technicaltestandroid.presentation.viewmodel.FavoriteViewModel
import com.example.technicaltestandroid.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
}