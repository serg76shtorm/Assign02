package com.sserhiichyk.assign02.com.sserhiichyk.assign02.data

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Constants {

    init {
        Log.i(
            "MainActivity", "Constants init ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )
    }

    const val idUser = "idUser"

    const val preferencesContacts = "appsetting"
    const val preferencesData = "pData"

    var userAvatar = ""
    var isMultiSelect: Boolean = false
    const val snackbarDuration = 5000
    var crutch = false

    //CustomButton
    const val marginGoogle = 16
    const val timeImageGoogle = 30
    const val timeTextGoogle = 40
    const val durationGoogle: Long = 1000L

}