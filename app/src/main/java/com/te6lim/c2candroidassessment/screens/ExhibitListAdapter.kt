package com.te6lim.c2candroidassessment.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.c2candroidassessment.R
import com.te6lim.c2candroidassessment.databinding.ItemExhibitBinding
import com.te6lim.c2candroidassessment.model.Exhibit

class ExhibitListAdapter : ListAdapter<Exhibit, ExhibitListAdapter.ExhibitViewHolder>(DiffCallbacks) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitViewHolder {
        return ExhibitViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ExhibitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExhibitViewHolder(
        private val itemExhibitBinding: ItemExhibitBinding
    ) : RecyclerView.ViewHolder(itemExhibitBinding.root) {

        companion object {
            fun create(parent: ViewGroup): ExhibitViewHolder {
                val itemBinding: ItemExhibitBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.item_exhibit, parent, false
                )
                itemBinding.recyclerView.adapter = ExhibitItemListAdapter()
                return ExhibitViewHolder(itemBinding)
            }
        }

        fun bind(exhibit: Exhibit) {
            itemExhibitBinding.titleHead.text = exhibit.title
            (itemExhibitBinding.recyclerView.adapter as ExhibitItemListAdapter).submitList(exhibit.images)
        }
    }

    private object DiffCallbacks : DiffUtil.ItemCallback<Exhibit>() {
        override fun areItemsTheSame(oldItem: Exhibit, newItem: Exhibit): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Exhibit, newItem: Exhibit): Boolean {
            return oldItem == newItem
        }

    }
}