package com.example.locusassignment.data.mapper

interface Mapper<in Model, out DomainModel> {
    fun toDomain(model: Model): DomainModel
}