package com.example.technicaltestandroid.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.domain.UserUseCase
import com.example.core.domain.entities.User

class HomeViewModel constructor(private val userUseCase: UserUseCase) : ViewModel() {

    val listUser: LiveData<PagingData<User>> =
        userUseCase.getAllUsers().asLiveData().cachedIn(viewModelScope)

    fun searchUsers(query: String): LiveData<PagingData<User>> {
        return userUseCase.searchUser(query)
            .asLiveData()
            .cachedIn(viewModelScope)
    }
}