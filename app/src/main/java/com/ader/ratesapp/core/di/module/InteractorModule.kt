package com.ader.ratesapp.core.di.module

import com.ader.ratesapp.core.db.AppDataBase
import com.ader.ratesapp.core.db.dao.RateDao
import com.ader.ratesapp.core.interactors.IRateInteractor
import com.ader.ratesapp.core.interactors.RateInteractor
import com.ader.ratesapp.core.repository.local.ILocalRateRepository
import com.ader.ratesapp.core.repository.local.LocalRateRepository
import com.ader.ratesapp.core.repository.remote.IRemoteRateRepository
import com.ader.ratesapp.core.repository.remote.RemoteRateRepository
import com.ader.ratesapp.core.server.RateApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApiModule::class, DatabaseModule::class])
class InteractorModule {
    @Provides
    @Singleton
    fun provideRateInteractor(remoteRepository: IRemoteRateRepository,
                              localRepository: ILocalRateRepository): IRateInteractor {
        return RateInteractor(remoteRepository, localRepository)
    }

    @Provides
    fun provideRemoteRateRepository(rateApi: RateApi): IRemoteRateRepository{
        return RemoteRateRepository(rateApi)
    }

    @Provides
    fun provideLocalRateRepository(appDataBase: AppDataBase): ILocalRateRepository{
        return LocalRateRepository(appDataBase.rateDao())
    }
}