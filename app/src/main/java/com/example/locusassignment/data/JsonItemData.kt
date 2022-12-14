package com.example.locusassignment.data

import com.google.gson.annotations.SerializedName

data class JsonItemData(
    @SerializedName("dataMap")
    val dataMap: DataMap?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: String?
) {
    data class DataMap(
        @SerializedName("options")
        var options: ArrayList<String?>?
    )
}
