package com.example.technicaltestandroid.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.UserUseCase

class FavoriteViewModel constructor(private val userUseCase: UserUseCase): ViewModel() {

    val listUserFavorite = userUseCase.getAllFavorite().asLiveData()

}