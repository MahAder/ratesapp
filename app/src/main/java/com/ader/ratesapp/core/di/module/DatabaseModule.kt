package com.ader.ratesapp.core.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ader.ratesapp.Constants
import com.ader.ratesapp.core.db.AppDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module()
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDataBase {
        return Room.databaseBuilder(context.applicationContext,
            AppDataBase::class.java,
            Constants.DATABASE_NAME).build()
    }
}