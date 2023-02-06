package com.sserhiichyk.assign02.com.sserhiichyk.assign02.recycler.utils

interface ItemTouchHelperAdapter {

    fun onItemClick(position: Int)
    fun onItemButtonClick(position: Int)
    fun onLongItemClick()
    fun onItemDismiss(positionAdapter: Int)

}