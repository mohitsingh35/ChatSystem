package com.ncs.chatsystem.firebaseauth.repository

import androidx.compose.runtime.MutableState

data class ModelUserResposne(
    val item: UserItems?,
    val key:String?="",

    ){

    data class UserItems(
        val userId:String?="",
        val name: String? ="",
    )

}