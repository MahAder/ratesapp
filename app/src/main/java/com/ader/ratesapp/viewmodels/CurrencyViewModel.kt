package com.ader.ratesapp.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ader.ratesapp.Constants
import com.ader.ratesapp.R
import com.ader.ratesapp.core.db.entities.RateEntity
import com.ader.ratesapp.core.interactors.IRateInteractor
import com.ader.ratesapp.ui.adapter.RateAdapterCurrencyModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CurrencyViewModel(val rateInteractor: IRateInteractor) : BaseViewModel() {
    val calculatedRateLiveData = MutableLiveData<List<RateAdapterCurrencyModel>>()

    var currentBaseValue = 0.0

    var baseCurrency = "EUR"

    var isOffline = MutableLiveData<Boolean>()

    init {
        isOffline.value = false
        updateBaseCurrency(baseCurrency, currentBaseValue)
    }

    private fun updateRateValues() {
        val disposable = Observable.just(currentBaseValue)
            .subscribeOn(Schedulers.io())
            .map {
                val rateList = rateInteractor.getRatesInternal()
                initImageHashMap(rateList)
                calculateRates(it, rateList)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                calculatedRateLiveData.value = it
            }, {})
    }

    fun updateValue(newValue: Double) {
        if (currentBaseValue == newValue) return
        currentBaseValue = newValue
        updateRateValues()
    }

    fun updateBaseCurrency(code: String, rateValue: Double) {
        stopDisposable()
        this.currentBaseValue = rateValue
        baseCurrency = code
        val dis = Observable.just(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadRates()
            }, {})
    }

    private fun calculateRateValuesInternal(): List<RateEntity> {
        val newBaseRateEntity = rateInteractor.getRateByCode(baseCurrency)
        val oldBaseValue = newBaseRateEntity.rateValue
        var newBaseRateInList = newBaseRateEntity

        val rateList = rateInteractor.getRatesInternal()
        for (rate in rateList) {
            rate.isBase = false
            val newValue = rate.rateValue / oldBaseValue
            rate.rateValue = newValue
            if (rate.rateCode == newBaseRateInList.rateCode) {
                newBaseRateInList = rate
            }
        }
        newBaseRateInList.isBase = true
        newBaseRateInList.rateValue = 1.0

        rateInteractor.updateRates(rateList)

        return rateList
    }

    private fun proceedNetworkError() {
        val dis = Observable.just(baseCurrency)
            .subscribeOn(Schedulers.io())
            .map {
                rateInteractor.updateRates(calculateRateValuesInternal())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateRateValues()
            }, {})
    }

    fun loadRates() {
        repeatApiCall(rateInteractor.loadRates(baseCurrency), object : Next<List<RateEntity>> {
            override fun onNext(t: List<RateEntity>) {
                isOffline.postValue(false)
                proceedNewRates(t)
            }
        }, object : Error {
            override fun onError(e: Throwable) {
                isOffline.postValue(true)
                proceedNetworkError()
            }
        })
    }

    fun proceedNewRates(rateEntityList: List<RateEntity>) {
        val obs = Observable.just(rateEntityList)
            .subscribeOn(Schedulers.io())
            .map {
                initImageHashMap(it)
                rateInteractor.updateRates(rateEntityList)
                updateRateValues()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }

    fun calculateRates(
        newValue: Double,
        rateEntityList: List<RateEntity>
    ): List<RateAdapterCurrencyModel> {
        val rateList = ArrayList<RateAdapterCurrencyModel>()

        for (rateEntity in rateEntityList) {
            val currentBaseValue: Double = newValue
            val revertedValue: Double = if (rateEntity.isBase) currentBaseValue else
                currentBaseValue * rateEntity.rateValue

            rateList.add(
                RateAdapterCurrencyModel(
                    rateEntity.rateCode,
                    round(revertedValue, 2),
                    rateEntity.isBase,
                    currencyImageHashMap[rateEntity.rateCode] ?: 0
                )
            )
            rateList.sortWith(Comparator { abc1, abc2 ->
                var a = java.lang.Boolean.compare(
                    abc2.isBase,
                    abc1.isBase
                )
                if(a == 0){
                    a = abc1.currencyName.compareTo(abc2.currencyName)
                }

                return@Comparator a
            })
        }

        return rateList
    }

    override fun onResume() {
        super.onResume()
    }

    fun round(value: Double, places: Int): Double {
        var value = value
        val factor = Math.pow(10.0, places.toDouble()).toLong()
        value = value * factor
        val tmp = Math.round(value)
        return tmp.toDouble() / factor
    }

    companion object {
        val currencyImageHashMap = HashMap<String, Int>()

        fun initImageHashMap(rateEntityList: List<RateEntity>) {
            if(currencyImageHashMap.isNotEmpty()) return
            for(rate in rateEntityList){
                currencyImageHashMap[rate.rateCode] = getCurrencyIcon(rate.rateCode)
            }
        }

        fun getCurrencyIcon(code: String): Int {
            return when (code.toLowerCase()) {
                Constants.BGN_CODE -> R.drawable.bgn
                Constants.AUD_CODE -> R.drawable.aud
                Constants.BRL_CODE -> R.drawable.brl
                Constants.CAD_CODE-> R.drawable.cad
                Constants.CHF_CODE -> R.drawable.chf
                Constants.CNY_CODE -> R.drawable.cny
                Constants.CZK_CODE -> R.drawable.czk
                Constants.DKK_CODE -> R.drawable.dkk
                Constants.EUR_CODE -> R.drawable.eur
                Constants.GBP_CODE -> R.drawable.gbp
                Constants.HKD_CODE -> R.drawable.hkd
                Constants.HRK_CODE -> R.drawable.hrk
                Constants.HUF_CODE -> R.drawable.huf
                Constants.IDR_CODE -> R.drawable.idr
                Constants.ILS_CODE -> R.drawable.ils
                Constants.INR_CODE -> R.drawable.inr
                Constants.ISK_CODE -> R.drawable.isk
                Constants.JPY_CODE -> R.drawable.jpy
                Constants.KRW_CODE -> R.drawable.krw
                Constants.MXN_CODE -> R.drawable.mxn
                Constants.MYR_CODE -> R.drawable.myr
                Constants.NOK_CODE -> R.drawable.nok
                Constants.NZD_CODE -> R.drawable.nzd
                Constants.PHP_CODE -> R.drawable.php
                Constants.PLN_CODE -> R.drawable.pln
                Constants.RON_CODE -> R.drawable.ron
                Constants.RUB_CODE -> R.drawable.rub
                Constants.SEK_CODE -> R.drawable.sek
                Constants.SGD_CODE -> R.drawable.sgd
                Constants.THB_CODE -> R.drawable.thb
                Constants.USD_CODE -> R.drawable.usd
                Constants.ZAR_CODE -> R.drawable.zar
                else -> 0
            }
        }
    }
}