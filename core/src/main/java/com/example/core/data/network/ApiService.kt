package com.example.core.data.network

import com.example.core.data.network.response.DetailResponse
import com.example.core.data.network.response.RepoResponseItem
import com.example.core.data.network.response.SearchUser
import com.example.core.data.network.response.UsersResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getAllUser(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): List<UsersResponseItem>

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String): Response<DetailResponse>

    @GET("users/{username}/repos")
    suspend fun getRepoUser(@Path("username") username: String): Response<List<RepoResponseItem>>

    @GET("search/users")
    suspend fun searchUser(
        @Query("q") username: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): SearchUser
}