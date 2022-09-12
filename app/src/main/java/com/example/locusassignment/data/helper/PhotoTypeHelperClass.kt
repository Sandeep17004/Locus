package com.example.locusassignment.data.helper

import android.net.Uri
import com.example.locusassignment.data.CustomItemViewType

data class PhotoTypeHelperClass(
    val imageUri: Uri?,
    val itemType: Int = CustomItemViewType.Photo.itemType
)
