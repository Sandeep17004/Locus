package com.example.locusassignment.data

import android.text.Editable

data class CommentModelClass(
    val itemType: Int = ItemDataType.Comment.itemType,
    val isSelected: Boolean = false,
    val comment: String? = null
)
