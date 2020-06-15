package com.ader.ratesapp.core.di.component

import com.ader.ratesapp.RatesApplication
import com.ader.ratesapp.core.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
        AppModule::class, ActivityBuilderModule::class, InteractorModule::class])
interface AppComponent : AndroidInjector<RatesApplication> {
    override fun inject(application: RatesApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: RatesApplication): Builder

        fun build(): AppComponent
    }
}