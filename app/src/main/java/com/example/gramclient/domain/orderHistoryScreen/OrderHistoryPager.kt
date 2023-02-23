package com.example.gramclient.domain.orderHistoryScreen

import androidx.paging.*
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gramclient.app.preference.CustomPreference
import com.example.gramclient.data.remote.ApplicationApi
import com.example.gramclient.domain.orderHistory.Data
import com.example.gramclient.domain.orderHistory.Pagination
import com.example.gramclient.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderHistoryPager(
    private val applicationApi: ApplicationApi,
    private val prefs: CustomPreference
) : PagingSource<Int, Data>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult.Page<Int, Data> {
        val pageNumber = params.key ?: 1
        val response = applicationApi.getOrderHistoryResponse(prefs.getAccessToken(),pageNumber)
        val prevKey = if (pageNumber > 0) pageNumber - 1 else null
        val nextKey = if (response.result.pagination!=null) pageNumber + 1 else null
        return LoadResult.Page(
                    data = response.result.data,
                    prevKey = prevKey,
                    nextKey = nextKey
        )
    }
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}

class OrderHistoryUseCase @Inject constructor(
    private val applicationApi: ApplicationApi,
    private val prefs: CustomPreference
) {
    fun response() = Pager(
        config = PagingConfig(
            pageSize = 10,
        ),
        pagingSourceFactory = {
            OrderHistoryPager(applicationApi, prefs)
        }
    ).flow

    operator fun invoke(pageSize: Int): Flow<PagingData<Data>> {
        return try {
            Pager(
                PagingConfig(pageSize = pageSize),
                pagingSourceFactory = { OrderHistoryPager(applicationApi, prefs) }
            ).flow
        } catch (e: Exception) {
            // Обработка ошибки, например, запись в лог или отображение сообщения пользователю
            throw Exception("Ошибка при загрузке данных: ${e.message}")
        }
    }
}
