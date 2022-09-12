package com.example.locusassignment.data

data class CustomItemData(
    var dataMap: DataMap,
    val id: String?,
    val title: String?,
    val type: String?
) {
    data class DataMap(
        var options: List<OptionData?>?
    ) {
        data class OptionData(
            var optionName: String,
            var isSelected: Boolean = false,
            val text: String = ""
        )
    }
}
