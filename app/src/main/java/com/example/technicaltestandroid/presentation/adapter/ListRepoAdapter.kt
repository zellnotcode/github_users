package com.example.technicaltestandroid.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.core.domain.entities.Repo
import com.example.technicaltestandroid.databinding.ItemRepoBinding
import com.example.technicaltestandroid.presentation.utils.RepoDiffUtil

class ListRepoAdapter : RecyclerView.Adapter<ListRepoAdapter.ViewHolder>() {
    private var listRepo = emptyList<Repo>()

    class ViewHolder(private var binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo) {
            binding.apply {
                tvName.text = repo.name
                tvVisibility.text = repo.visibility
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listRepo.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listRepo[position]
        holder.bind(item)
    }

    fun setData(data: List<Repo>) {
        val diffUtil = RepoDiffUtil(listRepo, data)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        listRepo = data
        diffResult.dispatchUpdatesTo(this)
    }

}