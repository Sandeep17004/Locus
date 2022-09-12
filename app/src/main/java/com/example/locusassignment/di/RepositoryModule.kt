package com.example.locusassignment.di

import com.example.locusassignment.data.mapper.CustomOptionsDataMapper
import com.example.locusassignment.data.mapper.CustomDataMapper
import com.example.locusassignment.data.mapper.CustomDataMapMapper
import org.koin.dsl.module

val RepositoryModule = module {
    factory { CustomOptionsDataMapper() }
    factory { CustomDataMapper(get()) }
    factory { CustomDataMapMapper(get()) }
}