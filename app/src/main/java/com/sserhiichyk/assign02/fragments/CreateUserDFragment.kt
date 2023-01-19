package com.sserhiichyk.assign02.com.sserhiichyk.assign02.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.sserhiichyk.assign02.R
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants.userAvatar
import com.sserhiichyk.assign02.data.DataListViewModel
import com.sserhiichyk.assign02.data.DataUser
import com.sserhiichyk.assign02.databinding.FragmentCreateUserBinding
import com.sserhiichyk.assign02.extensions.loadImageWithGlide
import java.util.*

class CreateUserDFragment : DialogFragment() {
    private lateinit var binding: FragmentCreateUserBinding
    private val dataListViewModel by lazy { DataListViewModel() }

    private val newUser = DataUser(
        dataListViewModel.getDataListSize(), "", "", "", 0,
        0, "", "", "", "", false, false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("requestKey") { key, bundle ->
            if (!bundle.isEmpty) {
                userAvatar = bundle.getString("bundleKey").toString()
                newUser.avatarUrl = userAvatar
                binding.imageView9.loadImageWithGlide(newUser.avatarUrl)
            }
        }

        //TODO: непойму в чем проблема? не работает если закоментировать
        setStyle(STYLE_NORMAL, R.style.BagDialogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateUserBinding.inflate(layoutInflater, container, false)

        val actCareer: AutoCompleteTextView = binding.actCareer
        val careerList = DataListViewModel().getCareerList()
        val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, careerList)
        actCareer.setAdapter(arrayAdapter)

        if (userAvatar.isNotEmpty()) {
            newUser.avatarUrl = userAvatar
            binding.imageView9.loadImageWithGlide(newUser.avatarUrl)
        }

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        with(binding) {
            button3.setOnClickListener {
                var errorText = ""

                if (teUsername.text!!.isEmpty() || newUser.avatarUrl.isEmpty()) {

                    if (teUsername.text!!.isEmpty()) errorText = errorText.plus("Введите Имя")
                    if (newUser.avatarUrl.isEmpty()) {
                        if (errorText.isNotEmpty()) errorText = errorText.plus(" , ")
                        errorText = errorText.plus("Выберите Аватар")
                    }

                    tiUsername.error = errorText
                    tiUsername.isErrorEnabled = true
                }

                if (!tiUsername.isErrorEnabled) {
                    newUser.id = dataListViewModel.getDataListSize()
                    newUser.name = teUsername.text.toString()
                    if (actCareer.text.isEmpty()) newUser.career = getString(R.string.jobless)
                    else newUser.career = actCareer.text.toString()
                    if (teEmail.text!!.isEmpty()) newUser.email =
                        newUser.name.lowercase().plus("@gmail.com")
                    else newUser.email = teEmail.text.toString()
                    if (tePhone.text!!.isEmpty()) newUser.phone = getString(R.string.phone_num)
                    else newUser.phone = tePhone.text.toString()
                    if (teAddress.text!!.isEmpty()) newUser.address =
                        "adress: ".plus(newUser.name.lowercase()).plus(" home")
                    else newUser.address = teAddress.text.toString()
                    if (teDateOfBirth.text!!.isEmpty()) newUser.dateOfBirth =
                        teDateOfBirth.hint.toString()
                    else newUser.dateOfBirth = teDateOfBirth.text.toString()

                    dataListViewModel.addUser(newUser)
                    val result = true
                    setFragmentResult("userCreat", bundleOf("creatUser" to result))
                    dismiss()
                }
            }

            imageView11.setOnClickListener {
                val avatarUserDFragment = AvatarUserDFragment()

                avatarUserDFragment.show(
                    (activity as AppCompatActivity).supportFragmentManager,
                    AvatarUserDFragment.TAG
                )
            }

            teUsername.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s!!.isNotEmpty()) {
                        tiUsername.isErrorEnabled = false
                        teEmail.hint = s.toString().lowercase().plus("@gmail.com")
                        teAddress.hint = "adress: ".plus(s.toString().lowercase()).plus(" home")
                    } else {
                        teEmail.hint = ""
                        teAddress.hint = ""
                    }
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.teDateOfBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(1995, 4, 12)
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                { _, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    binding.teDateOfBirth.setText(dat)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        binding.imageView7.setOnClickListener {
            dismiss()
        }

    }
}