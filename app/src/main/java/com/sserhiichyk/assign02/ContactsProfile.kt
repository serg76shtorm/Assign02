package com.sserhiichyk.assign02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sserhiichyk.assign02.databinding.ActivityContactsProfileBinding
import com.sserhiichyk.assign02.extensions.loadImageWithFresco
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class ContactsProfile : AppCompatActivity() {
    private lateinit var binding: ActivityContactsProfileBinding

    init {
        Log.i(
            "MainActivity", "ContactsProfile init ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactsProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val avatarNum: String = Random(System.currentTimeMillis()).nextInt(0, 78).toString()
        binding.imageView6.loadImageWithFresco("https://xsgames.co/randomusers/assets/avatars/male/$avatarNum.jpg")

        binding.button2.setOnClickListener {
            val intent = Intent(this, RecyclerActivityDel::class.java)
            startActivity(intent)
        }

    }
}