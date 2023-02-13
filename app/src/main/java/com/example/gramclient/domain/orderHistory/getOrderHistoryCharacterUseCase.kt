package com.example.gramclient.domain.orderHistory

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.orderHistoryScreen.orderHistoryResponse
import com.example.gramclient.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class getOrderHistoryCharacterUseCase @Inject constructor(
    private val repository: AppRepository
):PagingSource<Int,Result>() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val nextPage = params.key ?: 1
            val character= repository.getOrderHistoryCharacter(nextPage).result
            LoadResult.Page(
                data = character,
                prevKey = if(nextPage== 1)null else nextPage - 1,
                 nextKey = nextPage.plus(1)
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}