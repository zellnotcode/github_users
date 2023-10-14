package com.example.core.data.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.network.ApiService
import com.example.core.data.network.response.UsersResponseItem

class SearchPagingSource constructor(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, UsersResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, UsersResponseItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UsersResponseItem> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val responseData = apiService.searchUser(query, pageSize, page)
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (responseData.items.isEmpty()) null else page + 1
            LoadResult.Page(
                data = responseData.items,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}