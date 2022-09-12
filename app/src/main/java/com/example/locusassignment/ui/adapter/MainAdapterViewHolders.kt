package com.example.locusassignment.ui.adapter

import android.util.Log
import android.widget.RadioButton
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.locusassignment.R
import com.example.locusassignment.data.ItemData
import com.example.locusassignment.databinding.AdapterTypeCommentBinding
import com.example.locusassignment.databinding.AdapterTypePhotoBinding
import com.example.locusassignment.databinding.AdapterTypeSingleSelectionBinding

sealed class MainAdapterViewHolders(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class PhotoViewHolder(private val binding: AdapterTypePhotoBinding) :
        MainAdapterViewHolders(binding) {
        fun bind(data: ItemData, clickEventsHolder: ClickEventsHolder, listPosition: Int) {
            binding.apply {
                modelData = data
                ivHolderCancel.setOnClickListener {
                    clickEventsHolder.onDeleteImageClicked(listPosition)
                }
                ivHolderImage.setOnClickListener {
                    clickEventsHolder.pickImageFromCamera(listPosition)
                }
                Log.d("adapterData", "reached")
                if (data.dataMap?.options?.get(0)?.isNotEmpty() == true) {
                    Glide.with(binding.root.context).load(data.dataMap.options?.get(0)?.toUri())
                        .into(ivHolderImage)
                }
            }
        }
    }

    class SingleSelectionViewHolder(private val binding: AdapterTypeSingleSelectionBinding) :
        MainAdapterViewHolders(binding) {
        fun bind(
            data: ItemData,
            clickEventsHolder: ClickEventsHolder,
            listPosition: Int,
            selectedRadioButtonHashMap: HashMap<Int, Int>
        ) {
            binding.modelData = data
            data.dataMap?.options?.let { options ->
                binding.rgHolderOptions.removeAllViews()
                options.forEachIndexed { index, element ->
                    val radioButton = RadioButton(binding.root.context).apply {
                        id = index
                        text = element
                        setTextColor(binding.root.context.getColor(R.color.white))
                        setOnClickListener {
                            clickEventsHolder.onOptionSelected(
                                listPosition = listPosition,
                                groupPosition = index
                            )
                        }
                        if (selectedRadioButtonHashMap.containsKey(listPosition) && id == selectedRadioButtonHashMap[listPosition]
                        ) {
                            this.isChecked = true
                        }
                    }
                    binding.rgHolderOptions.addView(radioButton)
                }

            }
            binding.rgHolderOptions.setOnCheckedChangeListener { radioGroup, i ->
                radioGroup.findViewById<RadioButton>(i)?.let {
                    selectedRadioButtonHashMap[listPosition] =
                        it.id
                }
            }
        }
    }

    class CommentViewHolder(private val binding: AdapterTypeCommentBinding) :
        MainAdapterViewHolders(binding) {
        fun bind(data: ItemData, clickEventsHolder: ClickEventsHolder, listPosition: Int) {
            binding.modelData = data
        }
    }

}