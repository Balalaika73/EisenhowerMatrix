package com.example.eisenhowermatrix.RecyclerViewAdapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SimpleItemTouchHelperCallback (
    private val mAdapter: ItemTouchHelperAdapter
    ): ItemTouchHelper.Callback() {


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags : Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags :Int = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (target != null) {
            if (viewHolder != null) {
                mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition())
            }
        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder != null) {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition())
        }
    }
}