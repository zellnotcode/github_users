package com.example.core.domain

import androidx.paging.PagingData
import com.example.core.domain.entities.Repo
import com.example.core.domain.entities.User
import kotlinx.coroutines.flow.Flow

class UserInterceptor constructor(private val iRepository: IRepository) : UserUseCase {

    override fun getAllUsers(): Flow<PagingData<User>> =
        iRepository.getAllUsers()

    override fun getDetailUser(username: String): Flow<Resource<User>> =
        iRepository.getDetailUser(username)

    override fun searchUser(username: String): Flow<PagingData<User>> =
        iRepository.searchUser(username)

    override fun getAllFavorite(): Flow<Resource<List<User>>> =
        iRepository.getAllFavorite()

    override fun getRepoUser(username: String): Flow<Resource<List<Repo>>> =
        iRepository.getRepoUser(username)


    override suspend fun isFavoriteExist(login: String): Boolean =
        iRepository.isFavoriteExist(login)

    override suspend fun insertFavorite(user: User) =
        iRepository.insertFavorite(user)

    override suspend fun deleteFavorite(user: User) =
        iRepository.deleteFavorite(user)

}