package com.example.locusassignment.data

interface Mapper<in Model, out DomainModel> {
    fun toDomain(model: Model): DomainModel
}