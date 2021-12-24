package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    private var count = 0
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    companion object {
        const val VIEW_TYPE_DISABLED = 0
        const val VIEW_TYPE_ENABLED = 1
        const val MAX_POOL_SIZE = 15
    }

    var shopList = listOf<ShopItem>()
        set(value) {
            // Here we check the old list and the new list for differences
            val callback = ShopListDiffCallback(shopList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            // Set the new list
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("SHOP_LIST_ADAPTER", "count: ${++count}")

        val layoutId = when (viewType) {
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        val status = if (shopItem.enabled) { "Active" } else { "Not active" }

        holder.tvName.text = "${shopItem.name} $status"
        holder.tvCount.text = shopItem.count.toString()

        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }

        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    // Here we can set default values in view
    override fun onViewRecycled(holder: ShopItemViewHolder) {
        super.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = shopList[position]
        return if (shopItem.enabled) { VIEW_TYPE_ENABLED } else { VIEW_TYPE_DISABLED }
    }

    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }
}