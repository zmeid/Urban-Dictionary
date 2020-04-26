package com.zmeid.urbandictionary.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zmeid.urbandictionary.databinding.UrbanDefinitionRowBinding
import com.zmeid.urbandictionary.model.Urban
import javax.inject.Inject

class UrbanAdapter @Inject constructor() :
    ListAdapter<Urban, UrbanAdapter.UrbanViewHolder>(UrbanListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrbanViewHolder {
        return UrbanViewHolder(
            // Use view binding
            UrbanDefinitionRowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UrbanViewHolder, position: Int) {
        val urban = getItem(position)
        holder.bind(urban)
    }

    class UrbanViewHolder(private val binding: UrbanDefinitionRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(urban: Urban) {
            binding.textViewWord.text = urban.word
            binding.textViewDefinition.text = urban.definition
            binding.textViewExample.text = urban.example
            binding.textViewAuthor.text = urban.author
            binding.textViewThumbsUpCount.text = urban.thumbsUp.toString()
            binding.textViewThumbsDownCount.text = urban.thumbsDown.toString()
        }
    }
}

private class UrbanListDiffCallback : DiffUtil.ItemCallback<Urban>() {
    override fun areItemsTheSame(oldItem: Urban, newItem: Urban): Boolean {
        return false;
    }

    override fun areContentsTheSame(oldItem: Urban, newItem: Urban): Boolean {
        return oldItem == newItem
    }
}