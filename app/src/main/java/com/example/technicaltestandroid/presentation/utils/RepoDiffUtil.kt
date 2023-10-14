package com.example.technicaltestandroid.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.entities.Repo

class RepoDiffUtil(
    private val oldList: List<Repo>,
    private val newList: List<Repo>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].name != newList[newItemPosition].name -> {
                false
            }

            oldList[oldItemPosition].visibility != newList[newItemPosition].visibility -> {
                false
            }

            else -> true
        }
    }

}