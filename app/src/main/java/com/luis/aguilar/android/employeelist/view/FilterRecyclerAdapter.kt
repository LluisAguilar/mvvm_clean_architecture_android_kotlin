package com.luis.aguilar.android.employeelist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luis.aguilar.android.employeelist.databinding.ItemFilterBinding

class FilterRecyclerAdapter(private var filterList: List<String>, listener: FilterClickListener):
    RecyclerView.Adapter<FilterRecyclerAdapter.FilterViewHolder>() {

    private var itemClickListener: FilterClickListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): FilterViewHolder {
        val itemBinding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(itemBinding)
    }

    override fun onBindViewHolder(filterViewHolder: FilterViewHolder, position: Int) {
        filterViewHolder.bind(filterList, itemClickListener)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    class FilterViewHolder(private val itemBinding: ItemFilterBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(filterList: List<String>, listener: FilterClickListener) {

            itemBinding.itemFilterName.text = filterList[adapterPosition]

            itemBinding.itemFilterName.setOnClickListener {
                listener.onFilterClick(filterList[adapterPosition])
            }

        }
    }

    interface FilterClickListener {
        fun onFilterClick(filter:String)
    }
}