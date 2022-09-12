package com.example.locusassignment.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.locusassignment.data.CustomDataItem
import com.example.locusassignment.data.DataMapper
import com.example.locusassignment.data.ItemData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class MainViewModel(application: Application, private val dataMapper: DataMapper) :
    AndroidViewModel(application) {
    private val itemList = MutableLiveData<List<CustomDataItem>>()

    fun getItemList(): MutableLiveData<List<CustomDataItem>> {
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
            val mockedResponseType = object : TypeToken<List<ItemData>>() {}.type
            val response =
                gson.fromJson<List<ItemData>>(mockedResponseString, mockedResponseType)
                    .map { dataMapper.toDomain(it) }
            itemList.value = response
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}