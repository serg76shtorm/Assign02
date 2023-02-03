package com.sserhiichyk.assign02.com.sserhiichyk.assign02.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sserhiichyk.assign02.R
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants.idUser
import com.sserhiichyk.assign02.data.DataListViewModel
import com.sserhiichyk.assign02.data.UserModel
import com.sserhiichyk.assign02.databinding.ActivityContactsProfileBinding
import com.sserhiichyk.assign02.extensions.*

class ProfileUserDFragment : DialogFragment() {
    private lateinit var binding: ActivityContactsProfileBinding
    private var contact: Int = 0
    private lateinit var userModel: UserModel

    companion object {
        val TAG = ProfileUserDFragment::class.java.simpleName

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            contact = it.getInt(idUser)
        }

        userModel = DataListViewModel().infoUser(contact)
        setStyle(STYLE_NORMAL, R.style.BagDialogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityContactsProfileBinding.inflate(layoutInflater, container, false)

        binding.apply {
            textView4.invisible()
            button.invisible()
            button2.invisible()
            imageView10.visible()

            imageView6.loadImageWithFresco(userModel.avatarUrl)
            textView.text = userModel.name
            textView2.text = userModel.career
            textView3.text = userModel.address
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView10.setOnClickListener {
            dismiss()
        }
    }
}