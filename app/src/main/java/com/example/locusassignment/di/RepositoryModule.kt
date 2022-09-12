package com.example.locusassignment.di

import com.example.locusassignment.data.DataMapMapper
import com.example.locusassignment.data.DataMapper
import com.example.locusassignment.data.ListMapper
import org.koin.dsl.module

val RepositoryModule = module {
    factory { DataMapMapper() }
    factory { DataMapper(get()) }
    factory { ListMapper(get()) }
}