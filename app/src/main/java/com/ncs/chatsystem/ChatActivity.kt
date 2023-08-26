package com.ncs.chatsystem


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.ncs.authenticationapp.ui.theme.AuthViewmodel
import com.ncs.authenticationapp.utils.showMsg
import com.ncs.chatsystem.firebaseauth.repository.ModelUserResposne
import com.ncs.chatsystem.firebaseauth.repository.RealTimeModelResponse
import com.ncs.chatsystem.firebaseauth.repository.RealtimeRepository
import com.ncs.chatsystem.ui.theme.AuthScreenotp
import com.ncs.guessr.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val name=intent.getStringExtra("name")
            val id=intent.getStringExtra("id")
            chatHost(name!!, id!!)

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chatHost(name:String,id:String) {
    val viewModel: ChatViewModel = hiltViewModel()

    var message = remember {
        mutableStateOf("")
    }
    val res = viewModel.res.value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val senderid = FirebaseAuth.getInstance().currentUser?.uid
    val chatList= ArrayList<RealTimeModelResponse>()

    for (i in 0 until res.item.size){
        if (res.item[i].item!!.receiverId.equals(id)&&res.item[i].item!!.senderId.equals(senderid)||
            res.item[i].item!!.receiverId.equals(senderid)&&res.item[i].item!!.senderId.equals(id)){
            chatList.add(res.item[i])
        }
        Log.d("mohit111",chatList.toString())

    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.08f)
                .background(Color.LightGray), contentAlignment = Alignment.Center
        ) {
            Text(text = name, fontSize = 20.sp)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .background(Color.Green)
        ) {
            if (res.item.isNotEmpty()) {
                LazyColumn {
                    items(chatList) { chatItem ->
                        if (chatItem.item!!.senderId == senderid) {
                            messageSender(itemState = chatItem.item!!)
                        } else {
                            MessageReceiver(itemState = chatItem.item!!)
                        }
                    }
                }
            }

            if (res.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            if (res.error.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = res.error)
                }
            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()

                .background(Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = message.value,
                    onValueChange = { message.value = it },
                    placeholder = {
                        Text(
                            text = "Enter Message"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable {

                        scope.launch(Dispatchers.Main) {
                            if (message.value.isNotEmpty()){
                                viewModel
                                    .insert(
                                        RealTimeModelResponse.RealTimeItems
                                            (senderid, id, message.value)
                                    )
                                    .collect {
                                        when (it) {
                                            is ResultState.Success -> {
                                                context.showMsg(
                                                    msg = it.data
                                                )
                                                message.value = ""
                                            }

                                            is ResultState.Failure -> {
                                                context.showMsg(
                                                    msg = it.msg.toString()
                                                )
                                            }

                                            ResultState.Loading -> {
                                            }
                                        }
                                    }
                            }
                            else{
                                context.showMsg("Message cannot be Empty")
                            }

                        }
                    }, contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.Send, contentDescription = "")

                }
            }
        }
    }


}
@Composable
fun messageSender(itemState: RealTimeModelResponse.RealTimeItems) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = itemState.message!!)
        }
    }
}
@Composable
fun MessageReceiver(itemState: RealTimeModelResponse.RealTimeItems) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = itemState.message!!)
        }
    }
}

