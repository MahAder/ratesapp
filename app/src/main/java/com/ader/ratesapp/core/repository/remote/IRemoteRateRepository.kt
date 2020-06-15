package com.ader.ratesapp.core.repository.remote

import com.ader.ratesapp.core.server.model.RateModelResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver

interface IRemoteRateRepository {
    fun loadRates(base: String): Observable<RateModelResponse>
}