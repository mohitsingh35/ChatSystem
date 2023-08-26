package com.ncs.chatsystem

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ncs.chatsystem.firebaseauth.repository.RealTimeModelResponse
import com.ncs.chatsystem.firebaseauth.repository.RealtimeRepository
import com.ncs.guessr.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor
    (private val repo: RealtimeRepository):ViewModel(){
    private val _res:MutableState<MessageState> = mutableStateOf(MessageState())
    val res:State<MessageState> = _res
    fun insert(items: RealTimeModelResponse.RealTimeItems)=repo.insertMessage(items)

    init {
        viewModelScope.launch {
            repo.getMessage(senderId = FirebaseAuth.getInstance().currentUser?.uid!!, receiverId = "receiverId" ).collect{
                when(it){
                    is ResultState.Success->{
                        _res.value=MessageState(
                            item = it.data
                        )
                    }
                    is ResultState.Failure->{
                        _res.value=MessageState(
                            error = it.msg.toString()
                        )
                    }
                    ResultState.Loading->{
                        _res.value= MessageState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }


}

