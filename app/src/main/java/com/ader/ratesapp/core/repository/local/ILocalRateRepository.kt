package com.ader.ratesapp.core.repository.local

import androidx.lifecycle.LiveData
import com.ader.ratesapp.core.db.entities.RateEntity

interface ILocalRateRepository {
    fun insert(rateEntity: RateEntity)

    fun insertAll(rateEntityList: List<RateEntity>)

    fun update(rateEntity: RateEntity)

    fun delete(rateEntity: RateEntity)

    fun getAllRates(): List<RateEntity>

    fun updateBaseCurrency(newBaseCode: String)

    fun getRateByCode(code: String): RateEntity
}