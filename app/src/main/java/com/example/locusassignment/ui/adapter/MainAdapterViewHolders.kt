package com.example.locusassignment.ui.adapter

import android.view.View
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.locusassignment.R
import com.example.locusassignment.data.CustomItemData
import com.example.locusassignment.databinding.AdapterTypeCommentBinding
import com.example.locusassignment.databinding.AdapterTypePhotoBinding
import com.example.locusassignment.databinding.AdapterTypeSingleSelectionBinding

sealed class MainAdapterViewHolders(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class PhotoViewHolder(private val binding: AdapterTypePhotoBinding) :
        MainAdapterViewHolders(binding) {
        fun bind(data: CustomItemData, clickEventsHolder: ClickEventsHolder, listPosition: Int) {
            binding.apply {
                modelData = data
                ivHolderCancel.setOnClickListener {
                    clickEventsHolder.onDeleteImageClicked(listPosition)
                }
                ivHolderImage.setOnClickListener {
                    clickEventsHolder.pickImageFromCamera(listPosition)
                }
                if (data.dataMap.options?.get(0) != null) {
                    Glide.with(binding.root.context)
                        .load(data.dataMap.options?.get(0)?.optionName?.toUri()).centerCrop()
                        .into(ivHolderImage)
                } else {
                    ivHolderImage.setImageBitmap(null)
                }
            }
        }
    }

    class SingleSelectionViewHolder(private val binding: AdapterTypeSingleSelectionBinding) :
        MainAdapterViewHolders(binding) {
        fun bind(
            data: CustomItemData
        ) {
            binding.modelData = data
            data.dataMap.options?.let { options ->
                binding.rgHolderOptions.removeAllViews()
                options.forEachIndexed { index, element ->
                    val radioButton = RadioButton(binding.root.context).apply {
                        id = index
                        text = element?.optionName
                        setTextColor(
                            ContextCompat.getColorStateList(
                                binding.root.context,
                                R.color.white
                            )
                        )
                        data.dataMap.options?.let {
                            this.isChecked = it[index]?.isSelected == true
                        }
                    }
                    binding.rgHolderOptions.addView(radioButton)
                }
            }
            binding.rgHolderOptions.setOnCheckedChangeListener { radioGroup, i ->
                radioGroup.findViewById<RadioButton>(i)?.let { radioButton ->
                    data.dataMap.options?.get(radioButton.id)?.let {
                        data.dataMap.options?.forEachIndexed { index, element ->
                            element?.isSelected = index == radioButton.id
                        }
                    }
                }
            }
        }
    }

    class CommentViewHolder(private val binding: AdapterTypeCommentBinding) :
        MainAdapterViewHolders(binding) {
        fun bind(data: CustomItemData, clickEventsHolder: ClickEventsHolder, listPosition: Int) {
            binding.apply {
                modelData = data
                switchHolderComment.setOnCheckedChangeListener(null)
                data.dataMap.options?.let {
                    if (it[0]?.isSelected == true) {
                        switchHolderComment.isChecked = true
                        tvHolderComment.apply {
                            visibility = View.VISIBLE
                            if (it[0]?.text.equals("null")) {
                                setText("")
                            } else {
                                setText(it[0]?.text.toString())
                            }
                        }
                    } else {
                        switchHolderComment.isChecked = false
                        tvHolderComment.visibility = View.GONE
                    }
                } ?: kotlin.run {
                    switchHolderComment.isChecked = false
                    tvHolderComment.visibility = View.GONE
                }
                tvHolderComment.doAfterTextChanged {
                    clickEventsHolder.onCommentTextChanged(
                        listPosition,
                        it.toString()
                    )
                }
                switchHolderComment.setOnCheckedChangeListener { _, state ->
                    clickEventsHolder.onProvideCommentSelected(listPosition, state)
                }
            }
        }
    }
}

