package com.example.core.data

import androidx.paging.PagingData
import androidx.paging.map
import com.example.core.data.local.LocalDataSource
import com.example.core.data.network.NetworkDataSource
import com.example.core.data.network.response.ApiResponse
import com.example.core.domain.IRepository
import com.example.core.domain.Resource
import com.example.core.domain.entities.Repo
import com.example.core.domain.entities.User
import com.example.core.utlis.UserDataMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class Repository constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) : IRepository {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override fun getAllUsers(): Flow<PagingData<User>> {
        val pagingData = networkDataSource.getUsers()

        return pagingData.map { pagingDataResponse ->
            pagingDataResponse.map {
                UserDataMapper.userResponseToDomain(it)
            }
        }
    }

    override fun getDetailUser(username: String): Flow<Resource<User>> {
        return flow<Resource<User>> {
            emit(Resource.Loading())
            networkDataSource.getDetailUser(username).collect { result ->
                when (result) {
                    is ApiResponse.Success -> {
                        val userResponse = result.data
                        val data = UserDataMapper.detailResponseToDomain(userResponse)
                        emit(Resource.Success(data))
                    }

                    is ApiResponse.Empty -> {
                        emit(Resource.Error("Data Empty"))
                    }

                    is ApiResponse.Error -> {
                        emit(Resource.Error(result.errorMessage))
                    }
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun searchUser(username: String): Flow<PagingData<User>> {
        val pagingData = networkDataSource.searchUser(username)
        return pagingData.map { pagingDataResponse ->
            pagingDataResponse.map {
                UserDataMapper.userResponseToDomain(it)
            }
        }
    }

    override fun getRepoUser(username: String): Flow<Resource<List<Repo>>> {
        return flow<Resource<List<Repo>>> {
            emit(Resource.Loading())
            networkDataSource.getRepoUser(username).collect { result ->
                when (result) {
                    is ApiResponse.Success -> {
                        val response = result.data
                        val data = response.map {
                            UserDataMapper.repoResponseToDomain(it)
                        }
                        emit(Resource.Success(data))
                    }

                    is ApiResponse.Empty -> {
                        emit(Resource.Error("Data Empty"))
                    }

                    is ApiResponse.Error -> {
                        emit(Resource.Error(result.errorMessage))
                    }
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun getAllFavorite(): Flow<Resource<List<User>>> {
        return flow {
            emit(Resource.Loading())
            localDataSource.getAllFavorite().collect { listUser ->
                val listUserDomain = listUser.map {
                    UserDataMapper.userEntityToDomain(it)
                }
                emit(Resource.Success(listUserDomain))
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun isFavoriteExist(login: String): Boolean =
        withContext(ioDispatcher) {
            localDataSource.isFavoriteExist(login)
        }


    override suspend fun insertFavorite(user: User) {
        val userEntity = UserDataMapper.userDomainToEntity(user)
        withContext(ioDispatcher) {
            localDataSource.insertFavorite(userEntity)
        }
    }

    override suspend fun deleteFavorite(user: User) {
        val userEntity = UserDataMapper.userDomainToEntity(user)
        withContext(ioDispatcher) {
            localDataSource.deleteFavorite(userEntity)
        }
    }

}