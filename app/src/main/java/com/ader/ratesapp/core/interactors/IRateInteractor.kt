package com.ader.ratesapp.core.interactors

import androidx.lifecycle.LiveData
import com.ader.ratesapp.core.db.entities.RateEntity
import com.ader.ratesapp.core.server.model.RateModelResponse
import io.reactivex.Observable
import io.reactivex.Single

interface IRateInteractor {
    fun loadRates(base: String): Observable<List<RateEntity>>

    fun getRatesInternal(): List<RateEntity>

    fun updateBaseCurrency(code: String)

    fun getRateByCode(code: String): RateEntity

    fun updateRates(rateList: List<RateEntity>)
}