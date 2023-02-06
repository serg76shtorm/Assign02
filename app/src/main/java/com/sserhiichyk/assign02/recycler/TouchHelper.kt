package com.sserhiichyk.assign02.com.sserhiichyk.assign02.recycler

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sserhiichyk.assign02.R
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.recycler.utils.ItemTouchHelperAdapter
import kotlin.math.abs

class TouchHelper(private val adapter: ItemTouchHelperAdapter) :
    ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = 0//ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        //TODO: val - static?
        val trashBinIcon =
            ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_baseline_delete_sweep_24)
        val globalMiddle = 16// TODO: не добрался к ресурсам
        val globalTwoMini = 8
        val iconWidth = viewHolder.itemView.bottom - viewHolder.itemView.top - 2 * globalTwoMini

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            if (dX <= 0) {
                if (abs(dX) > iconWidth) {
                    c.clipRect(
                        dX, viewHolder.itemView.top.toFloat(),
                        viewHolder.itemView.right.toFloat(), viewHolder.itemView.bottom.toFloat()
                    )

                    c.drawColor(Color.RED)
                    val alpha = 1 - abs(dX * 1.2f) / recyclerView.width
                    //TODO: другой эфект???
                    viewHolder.itemView.alpha = alpha

                    trashBinIcon!!.bounds = Rect(
                        viewHolder.itemView.right - iconWidth - globalMiddle,
                        viewHolder.itemView.top + globalTwoMini,
                        viewHolder.itemView.right - globalMiddle,
                        viewHolder.itemView.bottom - globalTwoMini
                    )
                }
            } else if (dX > 0) {
                if (abs(dX) > iconWidth) {
                    c.clipRect(
                        0f, viewHolder.itemView.top.toFloat(),
                        dX, viewHolder.itemView.bottom.toFloat()
                    )

                    c.drawColor(Color.RED)
                    val alpha = 1 - abs(dX * 1.2f) / recyclerView.width
                    viewHolder.itemView.alpha = alpha

                    trashBinIcon!!.bounds = Rect(
                        globalMiddle,
                        viewHolder.itemView.top + globalTwoMini,
                        globalMiddle + iconWidth,
                        viewHolder.itemView.bottom - globalTwoMini
                    )
                }
            }

            trashBinIcon!!.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}