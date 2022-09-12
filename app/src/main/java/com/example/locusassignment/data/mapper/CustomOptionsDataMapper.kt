package com.example.locusassignment.data.mapper

import com.example.locusassignment.data.CustomItemData

class CustomOptionsDataMapper : Mapper<String, CustomItemData.DataMap.OptionData> {
    override fun toDomain(model: String): CustomItemData.DataMap.OptionData {
        return CustomItemData.DataMap.OptionData(model)
    }
}