package com.sserhiichyk.assign02.com.sserhiichyk.assign02.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.sserhiichyk.assign02.R
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.adapters.ImageAdapter
import com.sserhiichyk.assign02.databinding.FragmentAvatarUserBinding

class AvatarUserDFragment : DialogFragment() {
    private lateinit var binding: FragmentAvatarUserBinding
    private val imageAdapter by lazy { ImageAdapter(requireActivity()) }

    companion object {
        val TAG = AvatarUserDFragment::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BagDialogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAvatarUserBinding.inflate(layoutInflater, container, false)

        val courseAdapter = ImageAdapter(requireActivity())
        binding.gridView.adapter = courseAdapter

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        with(binding) {

            imageView3.setOnClickListener {
                dismiss()
            }

            gridView.setOnItemClickListener { parent, view, position, id ->
                val result = imageAdapter.listAvatar[position]
                setFragmentResult("requestKey", bundleOf("bundleKey" to result))

                dismiss()
            }

        }
    }

}