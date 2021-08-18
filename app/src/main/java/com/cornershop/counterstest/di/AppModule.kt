package com.cornershop.counterstest.di

import android.content.Context
import androidx.room.Room
import com.cornershop.counterstest.BuildConfig
import com.cornershop.counterstest.data.datasources.db.CounterDao
import com.cornershop.counterstest.data.datasources.db.CounterDataBase
import com.cornershop.counterstest.data.datasources.remote.ConnectivityInterceptor
import com.cornershop.counterstest.data.datasources.remote.RemoteDataService
import com.cornershop.counterstest.data.datasources.sharepreference.SharePreference
import com.cornershop.counterstest.data.utils.Utils
import com.cornershop.counterstest.data.utils.WifiService
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
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(
        connectivityInterceptor: ConnectivityInterceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(connectivityInterceptor)
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
            .baseUrl(Utils.API_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideCounterDataBase(@ApplicationContext appContext: Context): CounterDataBase {
        return Room.databaseBuilder(
            appContext,
            CounterDataBase::class.java,
            "cornershop.counterstest"
        ).build()
    }

    @Provides
    fun provideCounterDao(counterDataBase: CounterDataBase): CounterDao {
        return counterDataBase.counterDao()
    }

    @Singleton
    @Provides
    fun provideCurrencyService(retrofit: Retrofit): RemoteDataService =
        retrofit.create(RemoteDataService::class.java)

    @Singleton
    @Provides
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils =
        NetworkUtils(context)

    @Singleton
    @Provides
    fun provideWifiService(@ApplicationContext context: Context): WifiService =
        WifiService(context)

    @Provides
    fun providerInterceptor(wifiService: WifiService) = ConnectivityInterceptor(wifiService)

    @Provides
    fun providerSharePreference(@ApplicationContext context: Context) = SharePreference(context)

}