package com.ader.ratesapp.core.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ader.ratesapp.core.db.entities.RateEntity

@Dao
interface RateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rateEntity: RateEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rateEntityList: List<RateEntity>)

    @Query("SELECT * FROM rate")
    fun getAllRates(): List<RateEntity>

    @Query("SELECT * FROM rate WHERE isBase = 1")
    fun getBaseCurrency(): RateEntity

    @Query("SELECT * FROM rate WHERE rateCode = :code")
    fun getCurrencyByCode(code: String): RateEntity

    @Update
    fun update(rateEntity: RateEntity): Int

    @Update
    fun update(rateEntity: List<RateEntity>): Int

    @Delete
    fun delete(rateEntity: RateEntity)
}