package com.example.core.domain

import androidx.paging.PagingData
import com.example.core.domain.entities.Repo
import com.example.core.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface IRepository {

    fun getAllUsers() : Flow<PagingData<User>>

    fun getDetailUser(username: String) : Flow<Resource<User>>

    fun searchUser(username: String) : Flow<PagingData<User>>

    fun getRepoUser(username: String): Flow<Resource<List<Repo>>>

    fun getAllFavorite(): Flow<Resource<List<User>>>

    suspend fun isFavoriteExist(login: String) : Boolean

    suspend fun insertFavorite(user: User)

    suspend fun deleteFavorite(user: User)

}