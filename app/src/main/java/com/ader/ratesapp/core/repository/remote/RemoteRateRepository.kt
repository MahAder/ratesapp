package com.ader.ratesapp.core.repository.remote

import com.ader.ratesapp.core.di.module.ApiModule
import com.ader.ratesapp.core.server.RateApi
import com.ader.ratesapp.core.server.model.RateModelResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver

class RemoteRateRepository(val rateApi: RateApi): IRemoteRateRepository {
    override fun loadRates(base: String): Observable<RateModelResponse> {
        return rateApi.getRates(base)
    }
}