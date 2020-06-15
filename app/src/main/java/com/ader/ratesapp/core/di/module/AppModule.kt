package com.ader.ratesapp.core.di.module

import android.content.Context
import com.ader.ratesapp.RatesApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: RatesApplication): Context {
        return application
    }
}