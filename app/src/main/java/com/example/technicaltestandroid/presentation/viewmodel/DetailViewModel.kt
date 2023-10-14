package com.example.technicaltestandroid.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.Resource
import com.example.core.domain.UserUseCase
import com.example.core.domain.entities.Repo
import com.example.core.domain.entities.User
import kotlinx.coroutines.launch

class DetailViewModel constructor(private val userUseCase: UserUseCase) : ViewModel() {

    private val _detailLiveData: MutableLiveData<Resource<User>> = MutableLiveData()
    val detailLiveData: LiveData<Resource<User>> get() = _detailLiveData

    private val _repoLiveData: MutableLiveData<Resource<List<Repo>>> = MutableLiveData()
    val repoLiveData: LiveData<Resource<List<Repo>>> get() = _repoLiveData

    private val _isFavoriteLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isFavoriteLiveData: LiveData<Boolean> = _isFavoriteLiveData

    fun fetchUserDetail(username: String) {
        viewModelScope.launch {
            userUseCase.getDetailUser(username)
                .collect { resource ->
                    _detailLiveData.value = resource
                }
        }
    }

    fun fetchRepoDetail(username: String) {
        viewModelScope.launch {
            userUseCase.getRepoUser(username)
                .collect { resource ->
                    _repoLiveData.value = resource
                }
        }
    }

    fun isFavoriteExist(username: String) {
        viewModelScope.launch {
            val isFavorite = userUseCase.isFavoriteExist(username)
            _isFavoriteLiveData.value = isFavorite
        }
    }

    fun insertFavorite(user: User) = viewModelScope.launch {
        userUseCase.insertFavorite(user)
    }

    fun deleteFavorite(user: User) = viewModelScope.launch {
        userUseCase.deleteFavorite(user)
    }

}