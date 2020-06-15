package com.ader.ratesapp.core.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ader.ratesapp.Constants

@Entity(tableName = Constants.RATE_ENTITY_TABLE_NAME)
data class RateEntity(
    @PrimaryKey
    val rateCode: String,
    var rateValue: Double,
    var isBase: Boolean
)