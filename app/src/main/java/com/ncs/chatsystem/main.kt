package com.ncs.chatsystem

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.ncs.chatsystem.firebaseauth.repository.ModelUserResposne

@Composable
fun mainScreen(viewModel: MainActivityViewModel = hiltViewModel()){
    lateinit var authdb: FirebaseAuth
    authdb= FirebaseAuth.getInstance()
    val uid=authdb.currentUser?.uid
    val res=viewModel.res.value
    var user: ModelUserResposne.UserItems? =null

    Log.d("tag", res.item.toString())
    if(res.item.isNotEmpty()){
        for (i in 0 until  res.item.size){
            if (res.item[i].item?.userId==uid){
                user = res.item[i].item!!
            }
        }
        Column {
            Text(text = "Current User is ${user?.userId} && ${user?.name}")
            Spacer(modifier = Modifier.height(25.dp))
            LazyColumn{
                items(res.item, key = {
                    it.key!!
                }){res->
                    res.item?.let {
                        EachRowModerate(itemState = res.item)
                    }
                }
            }
        }

    }
    if(res.isLoading){
        Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center ){
            CircularProgressIndicator()
        }
    }
    if(res.error.isNotEmpty()){
        Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center ){
            Text(text = res.error)
        }
    }

}
@Composable
fun EachRowModerate(itemState: ModelUserResposne.UserItems){
    val context= LocalContext.current
    Box (modifier = Modifier
        .fillMaxWidth()
        .clickable {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("id", itemState.userId)
            intent.putExtra("name", itemState.name)
            context.startActivity(intent)
        }){
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)){
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Column {
                    Text(text = itemState.name.toString())
                }
            }
        }
    }
}
