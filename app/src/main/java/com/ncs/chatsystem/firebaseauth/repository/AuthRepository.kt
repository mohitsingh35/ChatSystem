package com.ncs.chatsystem.firebaseauth.repository

import android.app.Activity
import com.ncs.guessr.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {


    fun createUserWithPhone(
        phone:String,
        activity: Activity
    ):Flow<ResultState<String>>

    fun signInwithotp(
        otp:String
    ):Flow<ResultState<String>>
}
