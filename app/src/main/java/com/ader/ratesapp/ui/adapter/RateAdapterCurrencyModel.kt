package com.ader.ratesapp.ui.adapter

import com.ader.ratesapp.R
import java.util.*


class RateAdapterCurrencyModel(val rateCode: String, val rateValue: Double, val isBase: Boolean
                               ,val iconId: Int){
    val currencyName = Currency.getInstance(rateCode).getDisplayName(Locale.ENGLISH)
}