package com.example.marvelapp

import android.app.Application
import com.example.marvelapp.injection.utilsModule
import com.example.marvelapp.injection.databaseModule
import com.example.marvelapp.injection.apiModule
import com.example.marvelapp.injection.cacheModule
import com.example.marvelapp.injection.repoModule
import com.example.marvelapp.injection.useCaseModule
import com.example.marvelapp.injection.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MarvelApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@MarvelApp)
            modules(
                listOf(
                    utilsModule,
                    databaseModule,
                    apiModule,
                    cacheModule,
                    repoModule,
                    useCaseModule,
                    viewModelsModule
                )
            )
        }
    }
}
