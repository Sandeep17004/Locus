package com.example.locusassignment.ui.adapter

interface ClickEventsHolder {
    fun onDeleteImageClicked(listPosition: Int)
    fun onProvideCommentSelected(listPosition: Int, isSwitchedOn: Boolean)
    fun pickImageFromCamera(listPosition: Int)
    fun onCommentTextChanged(listPosition: Int, comment: String)
}