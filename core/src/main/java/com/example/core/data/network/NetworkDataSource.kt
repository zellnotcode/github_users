package com.example.core.data.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.data.network.pagingsource.SearchPagingSource
import com.example.core.data.network.pagingsource.UserPagingSource
import com.example.core.data.network.response.ApiResponse
import com.example.core.data.network.response.DetailResponse
import com.example.core.data.network.response.RepoResponseItem
import com.example.core.data.network.response.UsersResponseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkDataSource constructor(private val apiService: ApiService) {

    fun getUsers(): Flow<PagingData<UsersResponseItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                UserPagingSource(apiService)
            }
        ).flow
    }

    fun getDetailUser(username: String) : Flow<ApiResponse<DetailResponse>> = flow {
        try {
            val response = apiService.getDetailUser(username)
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    emit(ApiResponse.Success(user))
                } else {
                    emit(ApiResponse.Empty)
                }
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }

    fun getRepoUser(username: String) : Flow<ApiResponse<List<RepoResponseItem>>> = flow {
        try {
            val response = apiService.getRepoUser(username)

            if (response.isSuccessful) {
                val repo = response.body()
                if (repo.isNullOrEmpty()) {
                    emit(ApiResponse.Empty)
                } else {
                    emit(ApiResponse.Success(repo))
                }
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }

    fun searchUser(username: String) : Flow<PagingData<UsersResponseItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchPagingSource(apiService, username)
            }
        ).flow
    }
}