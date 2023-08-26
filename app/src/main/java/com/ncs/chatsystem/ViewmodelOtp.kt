package com.ncs.chatsystem

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.ncs.chatsystem.firebaseauth.repository.AuthRepository
import com.ncs.chatsystem.firebaseauth.repository.ModelUserResposne
import com.ncs.chatsystem.firebaseauth.repository.RealtimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewmodelOtp @Inject constructor(
    private val repo2: RealtimeRepository
) : ViewModel(){

    fun insertUser(item: ModelUserResposne.UserItems)=repo2.insertuser(item)

}