package com.gram.client.app.di

import android.content.Context
import androidx.room.Room
import com.gram.client.data.room.Dao.PostDao
import com.gram.client.data.room.Database.PostDatabase
import com.gram.client.app.preference.CustomPreference
import com.gram.client.data.AppFirebaseRepositoryImpl
import com.gram.client.data.AppRepositoryImpl
import com.gram.client.data.remote.ApplicationApi
import com.gram.client.data.room.Repository.RoomRepositoryImpl
import com.gram.client.domain.AppRepository
import com.gram.client.domain.FirebaseRepository
import com.gram.client.domain.RoomRepository
import com.gram.client.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
            .baseUrl(Constants.URL)
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

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): PostDatabase =
//        Room.databaseBuilder(context, PostDatabase::class.java,"postDatabase")
//            .build()
    Room.databaseBuilder(context, PostDatabase::class.java, "instruments")
    .createFromAsset("db/sqldb.db")
    .allowMainThreadQueries()
    .fallbackToDestructiveMigration()
    .build()


    @Provides
    fun providesPostDao(postDatabase: PostDatabase): PostDao =
        postDatabase.getPostDao()

    @Provides
    fun providesPostRepositoryImpl(postDao: PostDao): RoomRepositoryImpl =
        RoomRepositoryImpl(postDao)
    @Provides
    fun providesPostRepository(postDao: PostDao): RoomRepository =
        RoomRepositoryImpl(postDao)
}