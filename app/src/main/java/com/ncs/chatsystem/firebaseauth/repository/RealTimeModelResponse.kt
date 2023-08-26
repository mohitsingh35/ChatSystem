package com.ncs.chatsystem.firebaseauth.repository

data class RealTimeModelResponse(
    val item: RealTimeItems?,
    val key:String?="",

    ){

    data class RealTimeItems(
        val senderId:String?="",
        val receiverId:String?="",
        val message:String?="",
    )

}

