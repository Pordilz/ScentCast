package com.iu.scentcast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class FragranceAdapter : ListAdapter<Fragrance, FragranceAdapter.FragranceViewHolder>(FragranceComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragranceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fragrance, parent, false)
        return FragranceViewHolder(view)
    }

    override fun onBindViewHolder(holder: FragranceViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class FragranceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)

        fun bind(fragrance: Fragrance) {
            tvName.text = fragrance.name
            tvDetails.text = "${fragrance.house} • ${fragrance.season}"
        }
    }

    class FragranceComparator : DiffUtil.ItemCallback<Fragrance>() {
        override fun areItemsTheSame(oldItem: Fragrance, newItem: Fragrance): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Fragrance, newItem: Fragrance): Boolean {
            return oldItem == newItem
        }
    }
}