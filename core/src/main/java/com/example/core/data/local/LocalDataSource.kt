package com.example.core.data.local

import kotlinx.coroutines.flow.Flow

class LocalDataSource constructor(private val userDao: UserDao) {

    fun getAllFavorite(): Flow<List<UserEntity>> = userDao.getAllFavorite()

    fun isFavoriteExist(login: String): Boolean = userDao.isFavoriteExist(login)

    suspend fun insertFavorite(userEntity: UserEntity) = userDao.insertFavorite(userEntity)

    suspend fun deleteFavorite(userEntity: UserEntity) = userDao.deleteFavorite(userEntity)

}