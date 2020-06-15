package com.ader.ratesapp.core.repository.local

import androidx.lifecycle.LiveData
import com.ader.ratesapp.core.db.AppDataBase
import com.ader.ratesapp.core.db.dao.RateDao
import com.ader.ratesapp.core.db.entities.RateEntity

class LocalRateRepository(val rateDao: RateDao): ILocalRateRepository {
    override fun insert(rateEntity: RateEntity) {
        rateDao.insert(rateEntity)
    }

    override fun insertAll(rateEntityList: List<RateEntity>) {
        rateDao.insertAll(rateEntityList)
    }

    override fun update(rateEntity: RateEntity) {
        rateDao.update(rateEntity)
    }

    override fun delete(rateEntity: RateEntity) {
        rateDao.delete(rateEntity)
    }

    override fun getAllRates(): List<RateEntity> {
        return rateDao.getAllRates()
    }

    override fun updateBaseCurrency(newBaseCode: String) {
        val oldBaseCurrency = rateDao.getBaseCurrency()
        oldBaseCurrency.isBase = false

        val currencyToUpdate = rateDao.getCurrencyByCode(newBaseCode)
        currencyToUpdate.isBase = true

        rateDao.update(listOf(currencyToUpdate, oldBaseCurrency))
    }

    override fun getRateByCode(code: String): RateEntity {
        return rateDao.getCurrencyByCode(code)
    }
}