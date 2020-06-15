package com.ader.ratesapp.core.di.module

import com.ader.ratesapp.core.interactors.IRateInteractor
import com.ader.ratesapp.viewmodels.CurrencyViewModel
import dagger.Module
import dagger.Provides


@Module
class MainActivityModule {
    @Provides
    fun provideMainActivityViewModel(rateInteractor: IRateInteractor): CurrencyViewModel {
        return CurrencyViewModel(rateInteractor)
    }
}