package com.zmeid.urbandictionary.di.module

import com.zmeid.urbandictionary.repository.webservice.UrbanService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.zmeid.urbandictionary.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

/**
 * RetrofitModule is a module which is used inside RepositoryComponent.
 */
@Module
class RetrofitModule {

    /**
     * Provides Singleton retrofit instance.
     *
     * @return Retrofit instance.
     */
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides UrbanService
     *
     * @param retrofit instance to be used to create UrbanService.
     * @return UrbanService.
     */
    @Singleton
    @Provides
    fun provideUrbanService(retrofit: Retrofit): UrbanService {
        return retrofit.create(UrbanService::class.java)
    }
}