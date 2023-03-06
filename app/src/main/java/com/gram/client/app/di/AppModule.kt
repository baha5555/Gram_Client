package com.gram.client.app.di

import com.gram.client.app.preference.CustomPreference
import com.gram.client.utils.Constants
import com.gram.client.data.AppRepositoryImpl
import com.gram.client.data.AppFirebaseRepositoryImpl
import com.gram.client.data.remote.ApplicationApi
import com.gram.client.domain.AppRepository
import com.gram.client.domain.FirebaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationApi(): ApplicationApi =
        Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().setLevel(
                        HttpLoggingInterceptor.Level.BODY
                    )
                ).build()
            )
            .build()
            .create(ApplicationApi::class.java)

    @Provides
    @Singleton
    fun provideAppRepository(api: ApplicationApi, customPreference: CustomPreference): AppRepository =
        AppRepositoryImpl(api, customPreference)

    @Provides
    @Singleton
    fun provideFirebaseRepository(): FirebaseRepository = AppFirebaseRepositoryImpl()
}