package com.ader.ratesapp.viewmodels

import androidx.lifecycle.ViewModel
import com.ader.ratesapp.Constants
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Predicate
import java.util.logging.Handler

open class BaseViewModel: ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var lastDisposable: Disposable? = null
    private var resumed = AtomicBoolean()

    fun <T> repeatApiCall(observable: Observable<T>, next: Next<T>, error: Error?){
        resumed.set(true)
        lastDisposable = Observable.interval(0, Constants.RELOAD_RATES_TIME_MILLISECONDS, TimeUnit.MILLISECONDS)
            .filter{t -> resumed.get()}
            .flatMap {
                return@flatMap observable
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                next.onNext(it)
            }, {
                error?.onError(it)
            })

        compositeDisposable.add(lastDisposable!!)
    }

    fun stopDisposable(){
        compositeDisposable.clear()
    }

    fun onPause(){
        resumed.set(false)
    }

    open fun onResume(){
        resumed.set(true)
    }

    interface Next<T>{
        fun onNext(t: T)
    }

    interface Error{
        fun onError(e: Throwable)
    }
}