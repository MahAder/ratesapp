package com.ader.ratesapp.core.server.model

data class RateModelResponse(
    val baseCurrency: String,
    val rates: HashMap<String, Double>
)