package com.example.locusassignment.data

class ListMapper(private val dataMapper: DataMapMapper) :
    Mapper<ItemData.DataMap, CustomDataItem.DataMap> {
    override fun toDomain(model: ItemData.DataMap): CustomDataItem.DataMap {
        val dataMap = model.options?.map { it?.let { it1 -> dataMapper.toDomain(it1) } }
        return CustomDataItem.DataMap(dataMap)
    }
}