package com.te6lim.c2candroidassessment.screens.exhibitList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.c2candroidassessment.R
import com.te6lim.c2candroidassessment.databinding.ItemExhibitBinding
import com.te6lim.c2candroidassessment.model.Exhibit

class ExhibitListAdapter : ListAdapter<Exhibit, ExhibitListAdapter.ExhibitViewHolder>(DiffCallbacks) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitViewHolder {
        return ExhibitViewHolder.create(parent, ExhibitViewHolder.newAdapter())
    }

    override fun onBindViewHolder(holder: ExhibitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExhibitViewHolder(
        private val itemExhibitBinding: ItemExhibitBinding
    ) : RecyclerView.ViewHolder(itemExhibitBinding.root) {

        companion object {
            fun create(parent: ViewGroup, adapter: ExhibitItemListAdapter): ExhibitViewHolder {
                val itemBinding: ItemExhibitBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.item_exhibit, parent, false
                )
                itemBinding.recyclerView.adapter = adapter
                val snapHelper = LinearSnapHelper()
                snapHelper.attachToRecyclerView(itemBinding.recyclerView)
                return ExhibitViewHolder(itemBinding)
            }

            fun newAdapter() = ExhibitItemListAdapter()
        }

        fun bind(Exhibit: Exhibit) {
            itemExhibitBinding.titleHead.text = Exhibit.title
            (itemExhibitBinding.recyclerView.adapter as ExhibitItemListAdapter).submitList(Exhibit.images)
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