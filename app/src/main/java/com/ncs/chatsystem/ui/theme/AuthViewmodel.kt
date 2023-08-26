package com.ncs.authenticationapp.ui.theme

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.ncs.chatsystem.firebaseauth.repository.AuthRepository
import com.ncs.chatsystem.firebaseauth.repository.ModelUserResposne
import com.ncs.chatsystem.firebaseauth.repository.RealtimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(
    private val repo: AuthRepository
) :ViewModel(){


    fun createUserWithPhone(
        mobile:String,
        activity: Activity
    )=repo.createUserWithPhone(mobile,activity)

    fun signwithOtp(
        code:String
    )=repo.signInwithotp(code)
}