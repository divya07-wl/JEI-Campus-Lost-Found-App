package com.jei.lostfound.ui.myreports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jei.lostfound.data.entity.Item
import com.jei.lostfound.databinding.ItemMyReportBinding

class MyReportAdapter(
    private val onOpen: (Item) -> Unit,
    private val onDelete: (Item) -> Unit,
    private val onReturned: (Item) -> Unit
) : ListAdapter<Item, MyReportAdapter.Holder>(DIFF) {

    inner class Holder(private val binding: ItemMyReportBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.tvReportTitle.text = item.title
            binding.tvReportMeta.text = "${item.type} | ${item.location} | ${item.status}"
            val done = item.status == Item.Status.RETURNED
            binding.btnReturned.visibility = if (done) View.GONE else View.VISIBLE
            binding.cardReport.setOnClickListener { onOpen(item) }
            binding.btnDelete.setOnClickListener { onDelete(item) }
            binding.btnReturned.setOnClickListener { onReturned(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemMyReportBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(old: Item, new: Item) = old.itemId == new.itemId
            override fun areContentsTheSame(old: Item, new: Item) = old == new
        }
    }
}
