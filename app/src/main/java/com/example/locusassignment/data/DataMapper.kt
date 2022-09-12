package com.example.locusassignment.data

class DataMapper(private val listMapper: ListMapper) : Mapper<ItemData, CustomDataItem> {
    override fun toDomain(model: ItemData): CustomDataItem {
        return CustomDataItem(
            id = model.id,
            title = model.title,
            type = model.type,
            dataMap = model.dataMap.let { listMapper.toDomain(it!!) }
        )
    }
}