package com.iu.scentcast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for the RecyclerView in CollectionActivity.
 * Uses [ListAdapter] with [DiffUtil] for efficient list updates.
 * 
 * @property onEditClick Callback function triggered when the edit icon is clicked.
 * @property onDeleteClick Callback function triggered when the delete icon is clicked.
 */
class FragranceAdapter(
    private val onEditClick: (Fragrance) -> Unit,
    private val onDeleteClick: (Fragrance) -> Unit
) : ListAdapter<Fragrance, FragranceAdapter.FragranceViewHolder>(FragranceComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragranceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fragrance, parent, false)
        return FragranceViewHolder(view)
    }

    override fun onBindViewHolder(holder: FragranceViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    /**
     * ViewHolder class for fragrance list items.
     */
    inner class FragranceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
        private val btnEdit: ImageView = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)

        /**
         * Binds the fragrance data to the UI components.
         */
        fun bind(fragrance: Fragrance) {
            tvName.text = fragrance.name
            tvDetails.text = "${fragrance.house} • ${fragrance.season}"

            btnEdit.setOnClickListener { onEditClick(fragrance) }
            btnDelete.setOnClickListener { onDeleteClick(fragrance) }
        }
    }

    /**
     * Utility class for calculating the difference between two fragrance lists.
     */
    class FragranceComparator : DiffUtil.ItemCallback<Fragrance>() {
        override fun areItemsTheSame(oldItem: Fragrance, newItem: Fragrance): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Fragrance, newItem: Fragrance): Boolean {
            return oldItem == newItem
        }
    }
}