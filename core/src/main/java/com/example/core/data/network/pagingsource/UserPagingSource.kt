package com.example.core.data.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.network.ApiService
import com.example.core.data.network.response.UsersResponseItem

class UserPagingSource constructor(private val apiService: ApiService) : PagingSource<Int, UsersResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, UsersResponseItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UsersResponseItem> {
        return try {
            val since = params.key ?: 0
            val perPage = params.loadSize
            val responseData = apiService.getAllUser(since, perPage)
            val prevKey = if (since > 0) since - perPage else null
            val nextKey = if (responseData.isNotEmpty()) responseData.last().id else null
            LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = nextKey)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}