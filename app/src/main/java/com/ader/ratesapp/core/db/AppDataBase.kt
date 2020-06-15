package com.ader.ratesapp.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ader.ratesapp.Constants
import com.ader.ratesapp.core.db.dao.RateDao
import com.ader.ratesapp.core.db.entities.RateEntity

@Database(version = Constants.DATABASE_VERSION, entities = [RateEntity::class])
abstract class AppDataBase: RoomDatabase() {
    abstract fun rateDao(): RateDao
}