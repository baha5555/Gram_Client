package com.example.gramclient.domain.orderHistoryScreen

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gramclient.app.preference.CustomPreference
import com.example.gramclient.data.remote.ApplicationApi
import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderHistoryUseCase(
    private val applicationApi: ApplicationApi,
    private val prefs: CustomPreference
) : PagingSource<Int, OrderHistoryResult>() {

   /* operator fun invoke(): Flow<Resource<orderHistoryResponse>> =
        flow {
            try {
                emit(Resource.Loading<orderHistoryResponse>())

                val response: orderHistoryResponse = repository.getOrderHistory()
                emit(Resource.Success<orderHistoryResponse>(response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error<orderHistoryResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<orderHistoryResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<orderHistoryResponse>("${e.message}"))
            }
        }*/

    override fun getRefreshKey(state: PagingState<Int, OrderHistoryResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderHistoryResult> {
        return try {
            val page = params.key ?: 1
            val response = applicationApi.getOrderHistoryCharacter(page = page, token = prefs.getAccessToken())

            LoadResult.Page(
                data = response.result,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.result.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class NewsRepository @Inject constructor(
    private val applicationApi: ApplicationApi,
    private val prefs: CustomPreference
) {
    fun getPaging() = Pager(
        config = PagingConfig(
            pageSize = 20,
        ),
        pagingSourceFactory = {
            OrderHistoryUseCase(applicationApi, prefs)
        }
    ).flow
}