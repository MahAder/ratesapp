package com.ader.ratesapp.core.di.module

import com.ader.ratesapp.ui.MainActivity
import dagger.Module

import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity
}