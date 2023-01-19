package com.sserhiichyk.assign02.com.sserhiichyk.assign02.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.sserhiichyk.assign02.R
import com.sserhiichyk.assign02.extensions.loadImageWithGlide


class ImageAdapter(private val context: Context) : BaseAdapter() {
    val listAvatar = ArrayList<String>()

    init {
        loadAvatars()
    }

    override fun getCount(): Int {
        return listAvatar.size
    }

    override fun getItem(position: Int): Any {
        return listAvatar.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        lateinit var imageView: ImageView
        var listitemView = convertView

        if (listitemView == null) {
            listitemView =
                LayoutInflater.from(context).inflate(R.layout.gridview_item, parent, false)
        }

        imageView = listitemView!!.findViewById(R.id.iv_GridView)
        imageView.loadImageWithGlide(listAvatar[position])

        return listitemView
    }

    fun loadAvatars() {
        (48..78).forEach {
            listAvatar.add("https://xsgames.co/randomusers/assets/avatars/female/$it.jpg")
            listAvatar.add("https://xsgames.co/randomusers/assets/avatars/male/$it.jpg")
        }
    }

}