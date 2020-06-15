package com.ader.ratesapp.core.interactors

import androidx.lifecycle.LiveData
import com.ader.ratesapp.core.db.entities.RateEntity
import com.ader.ratesapp.core.repository.local.ILocalRateRepository
import com.ader.ratesapp.core.repository.remote.IRemoteRateRepository
import com.ader.ratesapp.core.server.model.RateModelResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

class RateInteractor(val remoteRateRepository: IRemoteRateRepository,
                     val localRateRepository: ILocalRateRepository): IRateInteractor {

    override fun loadRates(base: String): Observable<List<RateEntity>> {
        return remoteRateRepository.loadRates(base).map(Function<RateModelResponse, List<RateEntity>> {
            return@Function mapResponseToEntities(it)
        })
    }

    override fun getRatesInternal(): List<RateEntity> {
        return localRateRepository.getAllRates()
    }

    override fun updateBaseCurrency(code: String) {
        localRateRepository.updateBaseCurrency(code)
    }

    override fun getRateByCode(code: String): RateEntity {
        return localRateRepository.getRateByCode(code)
    }

    override fun updateRates(rateList: List<RateEntity>) {
        localRateRepository.insertAll(rateList)
    }

    private fun mapResponseToEntities(rateModelResponse: RateModelResponse): List<RateEntity>{
        val list = ArrayList<RateEntity>()
        list.add(createEntity(rateModelResponse.baseCurrency, 1.0, true))
        for (key in rateModelResponse.rates.keys){
            rateModelResponse.rates[key]?.let {
                list.add(createEntity(key, it, false))
            }
        }

        return list
    }

    private fun createEntity(code: String, value: Double, isBase: Boolean): RateEntity {
        return RateEntity(code, value, isBase)
    }
}