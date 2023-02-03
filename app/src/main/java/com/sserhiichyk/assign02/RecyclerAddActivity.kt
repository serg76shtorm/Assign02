package com.sserhiichyk.assign02

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants.isMultiSelect
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.fragments.CreateUserDFragment
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.fragments.ProfileUserDFragment
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.utils.ItemTouchHelperAdapter
import com.sserhiichyk.assign02.data.DataListViewModel
import com.sserhiichyk.assign02.data.UserModel
import com.sserhiichyk.assign02.databinding.ActivityRecyclerAddBinding
import com.sserhiichyk.assign02.extensions.gone
import com.sserhiichyk.assign02.extensions.invisible
import com.sserhiichyk.assign02.extensions.putIntArray
import com.sserhiichyk.assign02.extensions.visible
import com.sserhiichyk.assign02.recycler.RecyclerAdapterAdd
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecyclerAddActivity : AppCompatActivity(), ItemTouchHelperAdapter {
    private lateinit var binding: ActivityRecyclerAddBinding
    private lateinit var preferences: SharedPreferences

    private val recyclerAdapterAdd by lazy { RecyclerAdapterAdd(onContactListener = this) }
    val dataListViewModel by lazy { DataListViewModel() }

    init {
        Log.i(
            "MainActivity", "RecyclerAddActivity init ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewAdd.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAdd.adapter = recyclerAdapterAdd

        preferences = getSharedPreferences(Constants.preferencesContacts, Context.MODE_PRIVATE)

        supportFragmentManager.setFragmentResultListener("userCreat", this) { key, bundle ->
            val creatUser = bundle.getBoolean("creatUser", false)
            if (creatUser) {
                recreatList("")
            }
        }

        setListeners()

        Log.i(
            "MainActivity", "RecyclerAddActivity onCreate ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )

    }

    override fun onStart() {
        super.onStart()

        recreatList("")

        Log.i(
            "MainActivity",
            "RecyclerAddActivity onStart() called ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun recreatList(filterList: String) {
        recyclerAdapterAdd.datasetAdd = dataListViewModel.createUserListAdd()
        if (filterList.isEmpty()) recyclerAdapterAdd.arrayItem = recyclerAdapterAdd.datasetAdd
        else recyclerAdapterAdd.arrayItem = userListFilter(filterList)

        if (!isMultiSelect) dataListViewModel.arrayCheckInit(recyclerAdapterAdd.arrayItem, false)
        else {
            binding.checkBox2.visible()
            binding.imageView12.visible()
        }

        recyclerAdapterAdd.notifyDataSetChanged()
    }

    private fun userListFilter(filterList: String): ArrayList<UserModel> {
        val users: ArrayList<UserModel> = ArrayList()

        for (i in recyclerAdapterAdd.datasetAdd.indices) {
            if (recyclerAdapterAdd.datasetAdd[i].name.lowercase()
                    .indexOf(filterList.lowercase()) >= 0
            ) {
                users.add(recyclerAdapterAdd.datasetAdd[i])
            }
        }

        return users
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        with(binding) {

            imageView3.setOnClickListener {
                isMultiSelect = false
                finish()
            }

            imageView4.setOnClickListener {
                linearLayout1.invisible()
                linearLayout2.visible()
            }

            imageView2.setOnClickListener {
                linearLayout1.visible()
                linearLayout2.invisible()
                textInput.text!!.clear()
            }

            textInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    recreatList(s.toString())
                }
            })

            checkBox2.setOnCheckedChangeListener { _, isChecked ->
                dataListViewModel.arrayCheckInit(recyclerAdapterAdd.arrayItem, isChecked)
                recyclerAdapterAdd.notifyDataSetChanged()
            }

            recyclerViewAdd.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                val positionView =
                    (recyclerViewAdd.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if ((positionView > 3) && (!isMultiSelect)) imageView13.visible()
                else imageView13.invisible()
            }

            imageView13.setOnClickListener {
                recyclerViewAdd.smoothScrollToPosition(0)
            }

            imageView12.setOnClickListener {
                val count = dataListViewModel.arrayCheckedMove(recyclerAdapterAdd.arrayItem, true)
                var isDeleteCansel = true
                recreatList("")
                isMultiSelect = false
                checkBox2.gone()
                imageView12.invisible()
                if (count > 0) {
                    val deletedContacts =
                        if (count == 1) "Добавлен ".plus(count).plus(" контакт")
                        else "Добавлено ".plus(count).plus(" контактов")
                    val snackbar =
                        Snackbar.make(binding.root, deletedContacts, Constants.snackbarDuration)
                            .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                override fun onShown(transientBottomBar: Snackbar?) {
                                    super.onShown(transientBottomBar)
                                }

                                override fun onDismissed(
                                    transientBottomBar: Snackbar?,
                                    event: Int
                                ) {
                                    super.onDismissed(transientBottomBar, event)

                                    if (!isDeleteCansel) {
                                        dataListViewModel.resurrection()
                                        recreatList("")
                                    } else {
                                        val editor = preferences.edit()
                                        editor.putIntArray(
                                            Constants.preferencesData,
                                            dataListViewModel.arrayToPreferences()
                                        ).apply()
                                    }
                                }

                            }).setAction("Cansel") {
                                isDeleteCansel = false
                            }

                    snackbar.view.setBackgroundColor(getColor(R.color.c_dislike))
                    snackbar.show()
                }
            }

            customButton.setOnClickListener {
                Constants.userAvatar = ""
                CreateUserDFragment().show(supportFragmentManager, "CreateUserDFragment")
            }

        }
    }

    override fun onItemClick(position: Int) {
        val profileUserFragment = ProfileUserDFragment()
        val bundle = Bundle()

        bundle.putInt(Constants.idUser, position)
        profileUserFragment.arguments = bundle

        profileUserFragment.show(supportFragmentManager, ProfileUserDFragment.TAG)
    }

    override fun onItemButtonClick(position: Int) {
        val builder = AlertDialog.Builder(this)
        val dataUser = dataListViewModel.infoUser(position)
        var isDelete = true
        val snackbar = Snackbar.make(binding.root, dataUser.name, Constants.snackbarDuration)
            .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onShown(transientBottomBar: Snackbar?) {
                    super.onShown(transientBottomBar)
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)

                    if (!isDelete) {
                        dataListViewModel.resurrection()
                        recreatList("")
                    } else {
                        val editor = preferences.edit()
                        editor.putIntArray(
                            Constants.preferencesData,
                            dataListViewModel.arrayToPreferences()
                        ).apply()
                    }
                    dataListViewModel.setIsSelect(position, false)
                }

            }).setAction("Cansel") {
                isDelete = false
            }

        builder.setTitle("Добавить").setMessage(dataUser.name).setIcon(R.drawable.ic_like)
            .setCancelable(true).setPositiveButton("Нет") { _, _ -> }
            .setNegativeButton("Да") { _, _ ->
                dataListViewModel.setInContact(position, true)
                dataListViewModel.setIsSelect(position, true)
                recreatList("")
                snackbar.view.setBackgroundColor(getColor(R.color.c_dislike))
                snackbar.show()
            }

        builder.create().show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onLongItemClick() {
        isMultiSelect = !isMultiSelect

        with(binding) {
            if (isMultiSelect) {
                checkBox2.visible()
                imageView12.visible()
                imageView13.invisible()
            } else {
                checkBox2.gone()
                imageView12.invisible()
            }
        }
        dataListViewModel.arrayCheckInit(recyclerAdapterAdd.arrayItem, false)
        recyclerAdapterAdd.notifyDataSetChanged()
    }

    override fun onItemDismiss(positionAdapter: Int) {}

}