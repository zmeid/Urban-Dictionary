package com.zmeid.urbandictionary

import com.zmeid.urbandictionary.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication: DaggerApplication() {
    /**
     * It builds and returns AppComponent
     *
     * @return DaggerAppComponent
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}