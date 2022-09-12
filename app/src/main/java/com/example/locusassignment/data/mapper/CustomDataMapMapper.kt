package com.example.locusassignment.data.mapper

import com.example.locusassignment.data.CustomItemData
import com.example.locusassignment.data.JsonItemData

class CustomDataMapMapper(private val dataMapper: CustomOptionsDataMapper) :
    Mapper<JsonItemData.DataMap, CustomItemData.DataMap> {
    override fun toDomain(model: JsonItemData.DataMap): CustomItemData.DataMap {
        val dataMap = model.options?.map { it?.let { it1 -> dataMapper.toDomain(it1) } }
        return CustomItemData.DataMap(dataMap)
    }
}