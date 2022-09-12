package com.example.locusassignment.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.locusassignment.data.CustomItemData
import com.example.locusassignment.data.mapper.CustomDataMapper
import com.example.locusassignment.data.JsonItemData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class MainViewModel(application: Application, private val customDataMapper: CustomDataMapper) :
    AndroidViewModel(application) {
    private val itemList = MutableLiveData<List<CustomItemData>>()

    fun getItemList(): MutableLiveData<List<CustomItemData>> {
        return itemList
    }

    init {
        readMockedResponse()
    }

    private fun readMockedResponse() {
        try {
            val mockedResponseString =
                getApplication<Application>().assets.open("inputdata.json").bufferedReader()
                    .use { it.readText() }
            val gson = Gson()
            val mockedResponseType = object : TypeToken<List<JsonItemData>>() {}.type
            val response =
                gson.fromJson<List<JsonItemData>>(mockedResponseString, mockedResponseType)
                    .map { customDataMapper.toDomain(it) }
            itemList.value = response
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}