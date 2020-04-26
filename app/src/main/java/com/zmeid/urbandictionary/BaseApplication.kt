package com.zmeid.urbandictionary

import com.zmeid.urbandictionary.di.component.DaggerAppComponent
import com.zmeid.urbandictionary.util.TimberLineNumberDebugTree
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class BaseApplication : DaggerApplication() {
    @Inject
    lateinit var timberLineNumberDebugTree: TimberLineNumberDebugTree

    override fun onCreate() {
        super.onCreate()

        Timber.plant(timberLineNumberDebugTree)
    }

    /**
     * It builds and returns AppComponent
     *
     * @return DaggerAppComponent
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}