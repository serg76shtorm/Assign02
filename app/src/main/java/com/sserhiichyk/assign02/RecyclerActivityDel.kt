package com.sserhiichyk.assign02

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.data.Constants.isMultiSelect
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.fragments.ProfileUserDFragment
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.recycler.TouchHelper
import com.sserhiichyk.assign02.com.sserhiichyk.assign02.utils.ItemTouchHelperAdapter
import com.sserhiichyk.assign02.data.DataListViewModel
import com.sserhiichyk.assign02.data.DataUser
import com.sserhiichyk.assign02.databinding.ActivityRecyclerDelBinding
import com.sserhiichyk.assign02.extensions.*
import com.sserhiichyk.assign02.recycler.RecyclerAdapterDel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecyclerActivityDel : AppCompatActivity(), ItemTouchHelperAdapter {
    private lateinit var binding: ActivityRecyclerDelBinding
    private lateinit var preferences: SharedPreferences

    private val recyclerAdapterDel by lazy { RecyclerAdapterDel(onContactListener = this) }
    val dataListViewModel by lazy { DataListViewModel() }

    init {
        Log.i(
            "MainActivity", "RecyclerActivityDel init ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecyclerDelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPreferences()

        binding.recyclerViewDel.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDel.adapter = recyclerAdapterDel

        val callback: ItemTouchHelper.Callback = TouchHelper(this)
        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerViewDel)

        setListeners()

    }

    override fun onStart() {
        super.onStart()

        recreatList("")

        Log.i(
            "MainActivity",
            "RecyclerActivityDel onStart() called ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun recreatList(filterList: String) {
        recyclerAdapterDel.datasetDel = dataListViewModel.creatUserListDel()
        if (filterList.isEmpty()) recyclerAdapterDel.arrayItem = recyclerAdapterDel.datasetDel
        else recyclerAdapterDel.arrayItem = userListFilter(filterList)

        if (!isMultiSelect) dataListViewModel.arrayCheckInit(recyclerAdapterDel.arrayItem, false)
        else {
            binding.checkBox2.visible()
            binding.imageView12.visible()
        }

        Log.i(
            "MainActivity",
            "RecyclerActivityDel recreatList ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )

        recyclerAdapterDel.notifyDataSetChanged()
    }

    private fun userListFilter(filterList: String): ArrayList<DataUser> {
        val users: ArrayList<DataUser> = ArrayList()

        for (i in recyclerAdapterDel.datasetDel.indices) {
            if (recyclerAdapterDel.datasetDel[i].name.lowercase()
                    .indexOf(filterList.lowercase()) >= 0
            ) {
                users.add(recyclerAdapterDel.datasetDel[i])
            }
        }

        return users
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        with(binding) {

            textView.setOnClickListener {
                val intent = Intent(applicationContext, RecyclerAddActivity::class.java)
                isMultiSelect = false
                startActivity(intent)
            }

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
                dataListViewModel.arrayCheckInit(recyclerAdapterDel.arrayItem, isChecked)
                recyclerAdapterDel.notifyDataSetChanged()
            }

            recyclerViewDel.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                val positionView =
                    (recyclerViewDel.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if ((positionView > 3) && (!isMultiSelect)) imageView13.visible()
                else imageView13.invisible()
            }

            imageView13.setOnClickListener {
                recyclerViewDel.smoothScrollToPosition(0)
            }

            imageView12.setOnClickListener {
                val count = dataListViewModel.arrayCheckedMove(recyclerAdapterDel.arrayItem, false)
                var isDeleteCansel = true
                recreatList("")
                isMultiSelect = false
                checkBox2.gone()
                imageView12.invisible()
                if (count > 0) {
                    val deletedContacts =
                        if (count == 1) "Удален ".plus(count).plus(" контакт")
                        else "Удалено ".plus(count).plus(" контактов")
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

        }
    }

    private fun initPreferences() {
        val arrayInt: ArrayList<Int>
        var toPreferences = ""

        Log.i(
            "MainActivity", "RecyclerActivityDel initPreferences ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )

        preferences = getSharedPreferences(Constants.preferencesContacts, Context.MODE_PRIVATE)
        if (!Constants.crutch) {
            //TODO: повторная инициализация при повороте экрана

            if (preferences.contains(Constants.preferencesData)) {
                toPreferences = preferences.getString(Constants.preferencesData, "").toString()
            }

            if (toPreferences.isNotEmpty()) {
                arrayInt = preferences.getIntArray(toPreferences)
                for (i in arrayInt.indices) {
                    dataListViewModel.setInContact(arrayInt[i], true)
                }
            } else {
                arrayInt = arrayListOf(3, 21, 34, 46, 72, 93)

                for (i in arrayInt.indices) {
                    dataListViewModel.setInContact(arrayInt[i], true)
                }
            }
            Constants.crutch = true
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
        val male =
            if (dataUser.avatarUrl.indexOf("female") >= 0) R.string.unworthy2 else R.string.unworthy1
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

                        Toast.makeText(
                            applicationContext, "No",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        val editor = preferences.edit()
                        editor.putIntArray(
                            Constants.preferencesData,
                            dataListViewModel.arrayToPreferences()
                        ).apply()

                        Toast.makeText(
                            applicationContext, "Yes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    dataListViewModel.setIsSelect(position, false)
                }

            }).setAction("Cansel") {
                isDelete = false
            }


        builder.setTitle("Приговор").setMessage(dataUser.name).setIcon(R.drawable.ic_dislike)
            .setCancelable(true).setPositiveButton("Подумать") { _, _ -> }
            .setNegativeButton(male) { _, _ ->
                dataListViewModel.setInContact(position, false)
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
        dataListViewModel.arrayCheckInit(recyclerAdapterDel.arrayItem, false)
        recyclerAdapterDel.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemDismiss(positionAdapter: Int) {
        val dataUser = recyclerAdapterDel.arrayItem[positionAdapter]
        val position = dataUser.id

        dataListViewModel.setInContact(position, false)
        dataListViewModel.setIsSelect(position, true)
        recyclerAdapterDel.notifyItemRemoved(positionAdapter)

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
                    } else {
                        val editor = preferences.edit()
                        editor.putIntArray(
                            Constants.preferencesData,
                            dataListViewModel.arrayToPreferences()
                        ).apply()
                    }
                    dataListViewModel.setIsSelect(position, false)
                    recreatList("")
                }

            }).setAction("Cansel") {
                isDelete = false
            }

        snackbar.view.setBackgroundColor(getColor(R.color.c_dislike))
        snackbar.show()
    }
}