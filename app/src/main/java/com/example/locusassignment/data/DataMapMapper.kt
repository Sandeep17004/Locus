package com.example.locusassignment.data

class DataMapMapper : Mapper<String, CustomDataItem.DataMap.OptionData> {
    override fun toDomain(model: String): CustomDataItem.DataMap.OptionData {
        return CustomDataItem.DataMap.OptionData(model)
    }
}