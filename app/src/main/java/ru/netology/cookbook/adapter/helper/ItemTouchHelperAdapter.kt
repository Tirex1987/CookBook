package ru.netology.cookbook.adapter.helper

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onDropItem(fromPosition: Int, toPosition: Int)
}