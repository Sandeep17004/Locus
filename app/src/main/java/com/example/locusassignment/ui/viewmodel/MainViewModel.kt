package com.example.locusassignment.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.locusassignment.data.ItemData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val itemList = MutableLiveData<List<ItemData>>()

    fun getItemList(): MutableLiveData<List<ItemData>> {
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
            itemList.value = response
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}