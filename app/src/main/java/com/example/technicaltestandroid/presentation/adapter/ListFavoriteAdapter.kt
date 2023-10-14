package com.example.technicaltestandroid.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.domain.entities.User
import com.example.technicaltestandroid.databinding.ItemUserBinding
import com.example.technicaltestandroid.presentation.utils.FavoriteDiffUtil

class ListFavoriteAdapter(private val onClickItem: (login: String) -> Unit) : RecyclerView.Adapter<ListFavoriteAdapter.ViewHolder>() {

    private var listRepo = emptyList<User>()

    class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                tvUsername.text = user.login
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(ivUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listRepo.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listRepo[position]
        holder.bind(item)
        holder.binding.root.setOnClickListener {
            onClickItem(item.login.toString())
        }
    }

    fun setData(data: List<User>) {
        val diffUtil = FavoriteDiffUtil(listRepo, data)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        listRepo = data
        diffResult.dispatchUpdatesTo(this)
    }

}