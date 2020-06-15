package com.ader.ratesapp.ui

import android.os.Bundle
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ader.ratesapp.core.BaseActivity
import com.ader.ratesapp.core.db.entities.RateEntity
import com.ader.ratesapp.databinding.ActivityMainBinding
import com.ader.ratesapp.ui.adapter.RateAdapter
import com.ader.ratesapp.ui.adapter.RateAdapterCurrencyModel
import com.ader.ratesapp.viewmodels.CurrencyViewModel
import io.reactivex.Observer
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity: BaseActivity<CurrencyViewModel>() {
    @Inject
    lateinit var currencyViewModel: CurrencyViewModel

    var binding: ActivityMainBinding? = null

    var rateAdapter = RateAdapter()

    override fun getViewModel(): CurrencyViewModel {
        return currencyViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.viewModel = getViewModel()
        binding?.lifecycleOwner = this
        rateList.adapter = rateAdapter
        rateList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL
            ,false)

        rateAdapter.rateListener = object : RateAdapter.RateListener{
            override fun onRateClick(code: String, rateValue: Double) {
                getViewModel().updateBaseCurrency(code, rateValue)
            }

            override fun onValueChangeListener(value: Double) {
                getViewModel().updateValue(value)
            }

            override fun baseCurrencyChanged() {
                rateList.scrollToPosition(0)
            }
        }
    }

    object MainActivityBindingAdapter{
        @JvmStatic
        @BindingAdapter("rateList")
        fun setRateList(recyclerView: RecyclerView,
                        rateListCalculatedModel: List<RateAdapterCurrencyModel>?){
            rateListCalculatedModel?.let {
                (recyclerView.adapter as RateAdapter).setData(it)
            }
        }
    }
}