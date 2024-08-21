package com.develop.common.constants

import com.develop.common.R

enum class AppLanguage(
    val titleRes:Int,
    val country:String,
){
    EN(
        titleRes = R.string.app_english_lng_title,
        country = "us"
    ),
    UK(
        titleRes = R.string.app_ukraine_lng_title,
        country = "ua"
    )
}