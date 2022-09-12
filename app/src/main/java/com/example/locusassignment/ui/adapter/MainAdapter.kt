package com.example.locusassignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.locusassignment.data.CustomItemData
import com.example.locusassignment.data.CustomItemViewType
import com.example.locusassignment.databinding.AdapterTypeCommentBinding
import com.example.locusassignment.databinding.AdapterTypePhotoBinding
import com.example.locusassignment.databinding.AdapterTypeSingleSelectionBinding
import com.example.locusassignment.utils.Constants.TYPE_COMMENT
import com.example.locusassignment.utils.Constants.TYPE_PHOTO
import com.example.locusassignment.utils.Constants.TYPE_SINGLE_CHOICE
import java.lang.RuntimeException

class MainAdapter(private val clickEventsHolder: ClickEventsHolder) :
    ListAdapter<CustomItemData, MainAdapterViewHolders>(DiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapterViewHolders {
        return when (viewType) {
            CustomItemViewType.Photo.itemType -> {
                MainAdapterViewHolders.PhotoViewHolder(
                    AdapterTypePhotoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            CustomItemViewType.Comment.itemType -> {
                MainAdapterViewHolders.CommentViewHolder(
                    AdapterTypeCommentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            CustomItemViewType.SingleChoice.itemType -> {
                MainAdapterViewHolders.SingleSelectionViewHolder(
                    AdapterTypeSingleSelectionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw RuntimeException("can't create holder with type $viewType")
        }
    }

    override fun onBindViewHolder(holder: MainAdapterViewHolders, position: Int) {
        when (holder) {
            is MainAdapterViewHolders.PhotoViewHolder ->
                holder.bind(
                    getItem(holder.absoluteAdapterPosition), clickEventsHolder, position
                )
            is MainAdapterViewHolders.CommentViewHolder -> holder.bind(
                getItem(holder.absoluteAdapterPosition),
                clickEventsHolder, position
            )
            is MainAdapterViewHolders.SingleSelectionViewHolder -> holder.bind(
                getItem(holder.absoluteAdapterPosition)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getDataHolderType(currentList[position].type!!)
    }

    private fun getDataHolderType(type: String): Int {
        if (type.equals(TYPE_PHOTO, true)) {
            return CustomItemViewType.Photo.itemType
        } else if (type.equals(TYPE_SINGLE_CHOICE, true)) {
            return CustomItemViewType.SingleChoice.itemType
        } else if (type.equals(TYPE_COMMENT, true)) {
            return CustomItemViewType.Comment.itemType
        }
        return CustomItemViewType.Undefined.itemType
    }

    class DiffCallBack : DiffUtil.ItemCallback<CustomItemData>() {
        override fun areItemsTheSame(oldItem: CustomItemData, newItem: CustomItemData): Boolean =
            oldItem.dataMap == newItem.dataMap

        override fun areContentsTheSame(
            oldItem: CustomItemData,
            newItem: CustomItemData
        ): Boolean =
            oldItem == newItem
    }

}