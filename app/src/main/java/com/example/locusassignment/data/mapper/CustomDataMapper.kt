package com.example.locusassignment.data.mapper

import com.example.locusassignment.data.CustomItemData
import com.example.locusassignment.data.JsonItemData

class CustomDataMapper(private val customDataMapMapper: CustomDataMapMapper) :
    Mapper<JsonItemData, CustomItemData> {
    override fun toDomain(model: JsonItemData): CustomItemData {
        return CustomItemData(
            id = model.id,
            title = model.title,
            type = model.type,
            dataMap = model.dataMap.let { customDataMapMapper.toDomain(it!!) }
        )
    }
}