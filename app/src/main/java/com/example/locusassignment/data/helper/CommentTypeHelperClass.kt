package com.example.locusassignment.data.helper

import com.example.locusassignment.data.CustomItemViewType

data class CommentTypeHelperClass(
    val itemType: Int = CustomItemViewType.Comment.itemType,
    val isSelected: Boolean = false,
    val comment: String? = null
)
