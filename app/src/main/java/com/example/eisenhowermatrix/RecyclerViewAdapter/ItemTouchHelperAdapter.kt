package com.example.eisenhowermatrix.RecyclerViewAdapter

public interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}