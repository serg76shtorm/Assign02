package com.sserhiichyk.assign02.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sserhiichyk.assign02.databinding.ActivityItemRecyclerDelBinding
import com.sserhiichyk.assign02.extensions.loadImageLibrarys
import com.sserhiichyk.assign02.utils.DiffCallback
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.utils.ItemTouchHelperAdapter
import com.sserhiichyk.assign02.data.DataUser
import com.sserhiichyk.assign02.extensions.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecyclerAdapterDel(
    private val onContactListener: ItemTouchHelperAdapter
) : ListAdapter<DataUser, RecyclerAdapterDel.ViewHolder>(DiffCallback()) {

    lateinit var datasetDel: ArrayList<DataUser>
    lateinit var arrayItem: ArrayList<DataUser>

    init {

        Log.i(
            "MainActivity", "RecyclerAdapterDel init ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActivityItemRecyclerDelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.createHolder(position)
    }

    override fun getItemCount(): Int {
        return arrayItem.size
    }

    inner class ViewHolder(val binding: ActivityItemRecyclerDelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun createHolder(position: Int) {
            with(binding) {
                val itemDataUser = arrayItem.get(position)

                textView2.text = itemDataUser.name
                textView3.text = itemDataUser.career
                checkBox.setOnCheckedChangeListener(null)
                if (Constants.isMultiSelect) {
                    checkBox.isChecked = itemDataUser.isSelect
                    checkBox.visible()
                    imageButton.invisible()
                } else {
                    checkBox.gone()
                    imageButton.visible()
                }

                imageView.loadImageLibrarys(itemDataUser.avatarUrl, itemDataUser.imageLoad)

                imageButton.setOnClickListener {
                    onContactListener.onItemButtonClick(itemDataUser.id)
                }

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    itemDataUser.isSelect = isChecked
                }

                root.setOnClickListener {
                    onContactListener.onItemClick(itemDataUser.id)
                }

                root.setOnLongClickListener {
                    onContactListener.onLongItemClick()

                    return@setOnLongClickListener true
                }

            }
        }
    }

}