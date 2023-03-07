package com.gram.client.domain.orderHistoryScreen

import androidx.paging.*
import com.gram.client.app.preference.CustomPreference
import com.gram.client.data.remote.ApplicationApi
import com.gram.client.domain.orderHistory.Data
import kotlinx.coroutines.flow.Flow
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
