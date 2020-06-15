package com.ader.ratesapp.core.server

import com.ader.ratesapp.core.server.model.RateModelResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RateApi {
    @GET("https://hiring.revolut.codes/api/android/latest")
    fun getRates(@Query("base") base: String) : Observable<RateModelResponse>
}