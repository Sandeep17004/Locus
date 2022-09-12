package com.example.locusassignment.di

import android.app.Application
import com.example.locusassignment.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel {
        MainViewModel(get<Application>())
    }
}