package com.example.locusassignment.ui.adapter

interface ClickEventsHolder {
    fun onDeleteImageClicked(listPosition: Int)
    fun onOptionSelected(listPosition: Int, groupPosition: Int)
    fun onProvideCommentSelected(listPosition: Int)
    fun pickImageFromCamera(listPosition: Int)
}