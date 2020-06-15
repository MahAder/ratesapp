package com.ader.ratesapp.core.di.module

import com.ader.ratesapp.BuildConfig
import com.ader.ratesapp.core.server.RateApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideRetrofit(builder: Retrofit.Builder): Retrofit =
        builder.baseUrl(BuildConfig.HOST).build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .client(client)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRateApi(retrofit: Retrofit): RateApi{
        return retrofit.create(RateApi::class.java)
    }

    fun getLoggingInterceptor() : HttpLoggingInterceptor{
        val intereptor = HttpLoggingInterceptor()
        intereptor.level = HttpLoggingInterceptor.Level.BODY
        return intereptor
    }
}