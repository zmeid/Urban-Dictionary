package com.zmeid.urbandictionary.di.component

import android.app.Application
import com.zmeid.urbandictionary.BaseApplication
import com.zmeid.urbandictionary.di.module.ActivityBuildersModule
import com.zmeid.urbandictionary.di.module.RetrofitModule
import com.zmeid.urbandictionary.di.module.ViewModuleFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * AppComponent exist for the life time of application. It has the list of modules which are going to be used by application.
 */
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModuleFactoryModule::class,
        ActivityBuildersModule::class,
        RetrofitModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<BaseApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(baseApplication: BaseApplication)
}