package com.example.core.di

import androidx.room.Room
import com.example.core.data.Repository
import com.example.core.data.local.LocalDataSource
import com.example.core.data.local.UserDatabase
import com.example.core.data.network.ApiService
import com.example.core.data.network.NetworkDataSource
import com.example.core.domain.IRepository
import com.example.core.domain.UserInterceptor
import com.example.core.domain.UserUseCase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val coreModule = module {

    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "token {github token}")
                .build()
            chain.proceed(requestHeaders)
        }


        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ApiService::class.java)
    }

    single {
        Room.databaseBuilder(androidApplication(), UserDatabase::class.java, "user_database")
            .build()
    }

    factory {
        get<UserDatabase>().userDao()
    }

    single {
        LocalDataSource(get())
    }

    single {
        NetworkDataSource(get())
    }

    single<IRepository> {
        Repository(get(), get())
    }

    single<UserUseCase> {
        UserInterceptor(get())
    }
}