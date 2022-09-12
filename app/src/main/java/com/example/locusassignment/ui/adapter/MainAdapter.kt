package com.example.locusassignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.locusassignment.data.ItemData
import com.example.locusassignment.data.ItemDataType
import com.example.locusassignment.databinding.AdapterTypeCommentBinding
import com.example.locusassignment.databinding.AdapterTypePhotoBinding
import com.example.locusassignment.databinding.AdapterTypeSingleSelectionBinding
import com.example.locusassignment.utils.Constants.TYPE_COMMENT
import com.example.locusassignment.utils.Constants.TYPE_PHOTO
import com.example.locusassignment.utils.Constants.TYPE_SINGLE_CHOICE
import java.lang.RuntimeException

class MainAdapter(private val clickEventsHolder: ClickEventsHolder) :
    ListAdapter<ItemData, MainAdapterViewHolders>(DiffCallBack()) {
    var selectedRadioButtonHashMao = HashMap<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapterViewHolders {
        return when (viewType) {
            ItemDataType.Photo.itemType -> {
                MainAdapterViewHolders.PhotoViewHolder(
                    AdapterTypePhotoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            ItemDataType.Comment.itemType -> {
                MainAdapterViewHolders.CommentViewHolder(
                    AdapterTypeCommentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            ItemDataType.SingleChoice.itemType -> {
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
                getItem(holder.absoluteAdapterPosition),
                clickEventsHolder, position, selectedRadioButtonHashMao
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getDataHolderType(currentList[position].type!!)
    }

    private fun getDataHolderType(type: String): Int {
        if (type.equals(TYPE_PHOTO, true)) {
            return ItemDataType.Photo.itemType
        } else if (type.equals(TYPE_SINGLE_CHOICE, true)) {
            return ItemDataType.SingleChoice.itemType
        } else if (type.equals(TYPE_COMMENT, true)) {
            return ItemDataType.Comment.itemType
        }
        return ItemDataType.Undefined.itemType
    }

    class DiffCallBack : DiffUtil.ItemCallback<ItemData>() {
        override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean =
            oldItem.id == newItem.id && oldItem.dataMap == newItem.dataMap && oldItem.type == newItem.type && oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ItemData,
            newItem: ItemData
        ): Boolean =
            oldItem.id == newItem.id && oldItem.dataMap == newItem.dataMap && oldItem.type == newItem.type
    }

}