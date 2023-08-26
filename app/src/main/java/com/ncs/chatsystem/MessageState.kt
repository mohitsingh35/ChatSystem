package com.ncs.chatsystem

import com.ncs.chatsystem.firebaseauth.repository.RealTimeModelResponse

data class MessageState(

val item:List<RealTimeModelResponse> = emptyList(),
val error:String = "",
val isLoading:Boolean=false

)
