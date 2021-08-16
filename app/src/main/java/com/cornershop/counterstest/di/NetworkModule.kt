package com.cornershop.counterstest.di

import android.content.Context
import androidx.room.PrimaryKey
import com.cornershop.counterstest.BuildConfig
import com.cornershop.counterstest.data.datasources.db.CounterDao
import com.cornershop.counterstest.data.datasources.db.CounterDataBase
import com.cornershop.counterstest.data.datasources.remote.RemoteDataService
import com.cornershop.counterstest.presentation.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://127.0.0.1:3000/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideCurrencyService(retrofit: Retrofit): RemoteDataService =
        retrofit.create(RemoteDataService::class.java)

    @Singleton
    @Provides
    fun provideNetworkUtils( @ApplicationContext context: Context ) : NetworkUtils =
        NetworkUtils(context)

}