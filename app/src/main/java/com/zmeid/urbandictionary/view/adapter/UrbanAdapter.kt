package com.zmeid.urbandictionary.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zmeid.urbandictionary.databinding.UrbanDefinitionRowBinding
import com.zmeid.urbandictionary.model.Urban
import javax.inject.Inject

/**
 * Used as adapter of urban recycler view. View binding is used to bind the views.
 *
 * It uses Eugene W. Myers's difference algorithm to calculate the minimal number of updates to convert one list into another.
 *
 * It has [OnItemClickListener] to provide click listeners for share and play sound buttons.
 */
class UrbanAdapter @Inject constructor() :
    ListAdapter<Urban, UrbanAdapter.UrbanViewHolder>(UrbanListDiffCallback()) {

    private var listener: OnItemClickListener? = null

    fun setOnItemClickedListener(listener: OnItemClickListener) {
        this.listener = listener
    }

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

        holder.binding.imageViewShare.setOnClickListener { listener?.onShareClicked(urban.toString()) }
        holder.binding.imageViewPlaySound.setOnClickListener { listener?.onPlaySoundClicked(urban.soundUrl!!) }
    }

    class UrbanViewHolder(val binding: UrbanDefinitionRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(urban: Urban) {
            binding.textViewWord.text = urban.word
            binding.textViewDefinition.text = urban.definition
            binding.textViewExample.text = urban.example
            binding.textViewAuthor.text = urban.author
            binding.textViewThumbsUpCount.text = urban.thumbsUp.toString()
            binding.textViewThumbsDownCount.text = urban.thumbsDown.toString()
            if (!urban.soundUrl.isNullOrEmpty()) binding.imageViewPlaySound.visibility = View.VISIBLE else {
                binding.imageViewPlaySound.visibility = View.GONE
            }
        }
    }
}

interface OnItemClickListener {
    fun onShareClicked(text: String)
    fun onPlaySoundClicked(url: String)
}

private class UrbanListDiffCallback : DiffUtil.ItemCallback<Urban>() {
    override fun areItemsTheSame(oldItem: Urban, newItem: Urban): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Urban, newItem: Urban): Boolean {
        return oldItem == newItem
    }
}