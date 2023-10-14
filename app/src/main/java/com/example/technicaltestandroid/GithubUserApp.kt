package com.example.technicaltestandroid

import android.app.Application
import com.example.core.di.coreModule
import com.example.technicaltestandroid.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GithubUserApp : Application() {
    private val moduleList = arrayListOf(appModule, coreModule)

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@GithubUserApp)
        }
        loadKoinModules(
            moduleList
        )
    }

}