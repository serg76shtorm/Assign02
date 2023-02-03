package com.sserhiichyk.assign02.utils

import androidx.recyclerview.widget.DiffUtil
import com.sserhiichyk.assign02.data.UserModel

class DiffCallback : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return when {
            oldItem.id != newItem.id -> false
            oldItem.inContacts != newItem.inContacts -> false
            else -> true
        }
    }

}