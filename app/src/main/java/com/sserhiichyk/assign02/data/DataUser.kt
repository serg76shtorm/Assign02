package com.sserhiichyk.assign02.data

data class DataUser(
    var id: Int,
    var avatarUrl: String,
    var name: String,
    var career: String,
    var gender: Int,
    //-1 -male
    // 0 -any
    // 1 -female
    var imageLoad: Int,
    // 0 -Glide
    // 1 -Picasso
    // 2 -Fresco
    // 3 -UImageLoader
    var email: String,
    var phone: String,
    var address: String,
    var dateOfBirth: String,
    var inContacts: Boolean = false,
    //false -no
    // true -yes
    var isSelect: Boolean

)

data class DataMale(
    val name: String,
    val gender: Int
)

data class DataFemale(
    val name: String,
    val gender: Int
)
