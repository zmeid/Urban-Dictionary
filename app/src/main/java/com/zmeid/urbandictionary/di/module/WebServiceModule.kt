package com.zmeid.urbandictionary.di.module

import com.zmeid.urbandictionary.BuildConfig
import com.zmeid.urbandictionary.repository.webservice.UrbanService
import com.zmeid.urbandictionary.util.API_HOST_HEADER_TAG
import com.zmeid.urbandictionary.util.API_KEY_HEADER_TAG
import com.zmeid.urbandictionary.util.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * RetrofitModule is a module which is used inside RepositoryComponent.
 */
@Module
class WebServiceModule {
    //TODO surdaki herseyi apply ya cevir
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headerInterceptor)
            .build()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    fun provideHeaderInterceptor(): Interceptor = Interceptor { chain: Interceptor.Chain ->
        var request = chain.request()
        val headers = request.headers()
            .newBuilder()
            .add(API_KEY_HEADER_TAG, BuildConfig.API_KEY)
            .add(API_HOST_HEADER_TAG, BuildConfig.API_HOST)
            .build()
        request = request.newBuilder()
            .headers(headers)
            .build()
        chain.proceed(request)
    }

    /**
     * Provides Singleton retrofit instance.
     *
     * @return Retrofit instance.
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder().apply {
        client(okHttpClient)
        baseUrl(BASE_URL)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    /**
     * Provides UrbanService
     *
     * @param retrofit instance to be used to create UrbanService.
     * @return UrbanService.
     */
    @Singleton
    @Provides
    fun provideUrbanService(retrofit: Retrofit): UrbanService = retrofit.create(UrbanService::class.java)
}