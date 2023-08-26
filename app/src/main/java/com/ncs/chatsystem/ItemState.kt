package com.ncs.chatsystem

import com.ncs.chatsystem.firebaseauth.repository.ModelUserResposne

data class ItemState(
    val item:List<ModelUserResposne> = emptyList(),
    val error:String = "",
    val isLoading:Boolean=false
)