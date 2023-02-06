package com.example.gramclient.app.di

import com.example.gramclient.app.preference.CustomPreference
import com.example.gramclient.utils.Constants
import com.example.gramclient.data.AppRepositoryImpl
import com.example.gramclient.data.AppFirebaseRepositoryImpl
import com.example.gramclient.data.remote.ApplicationApi
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.FirebaseRepository
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