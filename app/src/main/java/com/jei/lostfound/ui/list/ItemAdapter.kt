package com.jei.lostfound.ui.list

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jei.lostfound.R
import com.jei.lostfound.data.entity.Item
import com.jei.lostfound.databinding.ItemLostFoundBinding

class ItemAdapter(
    private val onClick: (Item) -> Unit
) : ListAdapter<Item, ItemAdapter.Holder>(DIFF) {

    inner class Holder(private val binding: ItemLostFoundBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            val ctx = binding.root.context
            binding.tvTitle.text = item.title
            binding.tvType.text = item.type
            binding.tvStatus.text = item.status
            binding.tvMeta.text = ctx.getString(R.string.meta_format, item.category, item.location)
            binding.tvDate.text = item.itemDate
            binding.tvType.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(ctx, if (item.type == Item.Type.LOST) R.color.badge_lost else R.color.badge_found)
            )
            val statusColor = when (item.status) {
                Item.Status.RETURNED -> R.color.badge_returned
                Item.Status.CLAIMED -> R.color.badge_claimed
                else -> R.color.badge_open
            }
            binding.tvStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx, statusColor))
            binding.cardItem.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemLostFoundBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(old: Item, new: Item) = old.itemId == new.itemId
            override fun areContentsTheSame(old: Item, new: Item) = old == new
        }
    }
}
