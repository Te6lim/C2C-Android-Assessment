package com.te6lim.c2candroidassessment.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.te6lim.c2candroidassessment.R
import com.te6lim.c2candroidassessment.databinding.ItemImageBinding

class ExhibitItemListAdapter :
    ListAdapter<String, ExhibitItemListAdapter.ImageViewHolder>(DiffUtilCallbacks) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImageViewHolder(
        private val imageBinding: ItemImageBinding
    ) : RecyclerView.ViewHolder(imageBinding.root) {
        companion object {
            fun create(parent: ViewGroup): ImageViewHolder {
                val itemBinding: ItemImageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.item_image, parent, false
                )
                return ImageViewHolder(itemBinding)
            }
        }

        fun bind(imageUrl: String) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(imageBinding.imageView2)
        }
    }

    private object DiffUtilCallbacks : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
}