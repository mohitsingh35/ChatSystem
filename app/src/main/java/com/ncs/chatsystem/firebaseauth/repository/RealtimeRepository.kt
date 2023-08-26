package com.ncs.chatsystem.firebaseauth.repository

import android.net.Uri
import com.ncs.guessr.utils.ResultState
import kotlinx.coroutines.flow.Flow


interface RealtimeRepository {
    fun insertMessage(
        item: RealTimeModelResponse.RealTimeItems
    ): Flow<ResultState<String>>
    fun insertuser(
        item: ModelUserResposne.UserItems
    ): Flow<ResultState<String>>

    fun getMessage(senderId:String,receiverId:String):Flow<ResultState<List<RealTimeModelResponse>>>
    fun getUser():Flow<ResultState<List<ModelUserResposne>>>



}