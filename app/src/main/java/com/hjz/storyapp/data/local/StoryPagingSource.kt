package com.hjz.storyapp.data.local

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hjz.storyapp.data.api.ApiConfigStory
import com.hjz.storyapp.data.api.ApiService
import com.hjz.storyapp.data.pref.UserPreference
import com.hjz.storyapp.data.response.ListStoryItem
import kotlinx.coroutines.flow.first

class StoryPagingSource (
    private val apiService : ApiService,
    private val userPreference: UserPreference
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        val position = params.key ?: INITIAL_PAGE_INDEX
        val token = userPreference.getSession().first().token
        Log.d("TokenStory", token)

        val response = ApiConfigStory.getApiService(token).getStory(position, params.loadSize)
        Log.d("StoryPagingSource", response.body()?.listStory.toString())
        return try {
            if(response.body()?.listStory.isNullOrEmpty()) {
                LoadResult.Error(Exception("Empty response body"))
            } else {
                LoadResult.Page(
                    data = response.body()?.listStory ?: emptyList(),
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (response.body()?.listStory.isNullOrEmpty()) null else position + 1
                )
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}