package com.ader.ratesapp.ui.adapter

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ader.ratesapp.R
import com.ader.ratesapp.generated.callback.OnClickListener
import com.ader.ratesapp.viewmodels.CurrencyViewModel
import kotlinx.android.synthetic.main.item_rate.view.*
import java.math.BigDecimal

class RateAdapter : RecyclerView.Adapter<RateAdapter.RateViewHolder>() {
    private val rateListCalculatedModel = ArrayList<RateAdapterCurrencyModel>()

    var rateListener: RateListener? = null

    var baseValueEditText: EditText? = null

    val textChacngeListener = createTexChangeListener()

    var pendingPosition = -1

    init {
        setHasStableIds(true)
    }

    fun setData(rateListCalculatedModel: List<RateAdapterCurrencyModel>) {
        this.rateListCalculatedModel.clear()
        this.rateListCalculatedModel.addAll(rateListCalculatedModel)
        notifyDataSetChanged()
        if (pendingPosition > -1) {
            rateListener?.baseCurrencyChanged()
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return if (viewType == ACTIVE_ITEM_VIEW_TYPE) ActiveRateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rate_base,
                parent, false
            )
        ) else return RateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rate,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return rateListCalculatedModel.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ACTIVE_ITEM_VIEW_TYPE else ITEM_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(rateListCalculatedModel[position], position)
    }

    open inner class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(rateCalculatedModel: RateAdapterCurrencyModel, position: Int) {
            with(itemView) {
                val text = if (rateCalculatedModel.rateValue == 0.0) ""
                else rateCalculatedModel.rateValue.toString()

                setText(rateValue, text)

                if (rateCode.text != rateCalculatedModel.rateCode) {
                    rateCode.text = rateCalculatedModel.rateCode
                }

                if (currencyIconIv.tag != rateCalculatedModel.iconId) {
                    setImage(currencyIconIv, rateCalculatedModel.iconId)
                }

                if (rateName.text != rateCalculatedModel.currencyName) {
                    rateName.text = rateCalculatedModel.currencyName
                }

                setClickListener(itemView,
                    View.OnClickListener {
                        pendingPosition = position
                        rateListener?.onRateClick(
                            rateCalculatedModel.rateCode,
                            convertStringToDouble(rateValue.text.toString())
                        )
                    })
            }
        }

        open fun setText(editText: EditText, text: String){
            if (editText.text.toString() != text) {
                editText.setText(text)
            }
        }

        private fun setImage(imageView: ImageView, id: Int) {
            imageView.setImageResource(id)
            imageView.tag = id
        }

        open fun setClickListener(view: View, clickListener: View.OnClickListener){
            view.setOnClickListener(clickListener)
        }
    }

    fun convertStringToDouble(s: String): Double {
        try {
            return s.toDouble()
        } catch (e: java.lang.Exception) {
            return 0.0
        }
    }

    open inner class ActiveRateViewHolder(itemView: View) : RateViewHolder(itemView) {
        override fun bind(rateCalculatedModel: RateAdapterCurrencyModel, position: Int) {
            super.bind(rateCalculatedModel, position)
            with(itemView) {
                setTextListener(rateValue)

                if (pendingPosition > -1) {
                    baseValueEditText?.setText(rateCalculatedModel.rateValue.toString())
                    baseValueEditText?.requestFocus()
                    pendingPosition = -1
                }
            }
        }

        override fun setText(editText: EditText, text: String) {

        }

        override fun setClickListener(view: View, clickListener: View.OnClickListener) {

        }

        private fun setTextListener(editText: EditText) {
            baseValueEditText = editText
            baseValueEditText!!.addTextChangedListener(textChacngeListener)
            baseValueEditText!!.onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        baseValueEditText?.setSelection(baseValueEditText?.text?.length ?: 0)
                    }
                }
        }
    }

    fun createTexChangeListener(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    rateListener?.onValueChangeListener(s.toString().toDouble())
                } catch (e: Exception) {
                    rateListener?.onValueChangeListener(0.0)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        }
    }

    companion object {
        private val ACTIVE_ITEM_VIEW_TYPE = 0
        private val ITEM_VIEW_TYPE = 1
    }

    interface RateListener {
        fun onRateClick(code: String, rateValue: Double)

        fun onValueChangeListener(value: Double)

        fun baseCurrencyChanged()
    }
}