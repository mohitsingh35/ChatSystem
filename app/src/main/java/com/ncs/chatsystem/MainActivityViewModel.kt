package com.ncs.chatsystem


import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncs.chatsystem.firebaseauth.repository.RealTimeModelResponse
import com.ncs.chatsystem.firebaseauth.repository.RealtimeRepository
import com.ncs.guessr.utils.ResultState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor
    (private val repo: RealtimeRepository):ViewModel(){
    private val _res:MutableState<ItemState> = mutableStateOf(
        ItemState()
    )
    val res:State<ItemState> = _res

    init {
        viewModelScope.launch {
            repo.getUser().collect{
                when(it){
                    is ResultState.Success->{
                        _res.value= ItemState(
                            item = it.data
                        )
                    }
                    is ResultState.Failure->{
                        _res.value= ItemState(
                            error = it.msg.toString()
                        )
                    }
                    ResultState.Loading->{
                        _res.value= ItemState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

}