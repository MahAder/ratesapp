package com.ader.ratesapp.core

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.ader.ratesapp.viewmodels.BaseViewModel
import dagger.android.support.DaggerAppCompatActivity


abstract class BaseActivity<T : BaseViewModel?> : DaggerAppCompatActivity() {
    private var viewModel: T? = null

    abstract fun getViewModel(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = if (viewModel == null) getViewModel() else viewModel
    }

    override fun onPause() {
        super.onPause()
        viewModel?.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel?.onResume()
    }
}