package com.ncs.chatsystem


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.ncs.authenticationapp.ui.theme.AuthViewmodel
import com.ncs.authenticationapp.utils.showMsg
import com.ncs.chatsystem.firebaseauth.repository.ModelUserResposne
import com.ncs.chatsystem.ui.theme.AuthScreenotp
import com.ncs.guessr.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsEnterActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            details()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun details(viewmodel: ViewmodelOtp = hiltViewModel()){
    val context= LocalContext.current
    var text = remember {
        mutableStateOf("")
    }

    val scope= rememberCoroutineScope()
    lateinit var authdb: FirebaseAuth
    authdb = FirebaseAuth.getInstance()
    var move by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column {
            OutlinedTextField(value = text.value, onValueChange = { text.value=it }, label = { Text(text = "Name") })
            val item= ModelUserResposne.UserItems(authdb.currentUser?.uid,text.value)
            Log.d("item",item.toString())
            Button(onClick = { scope.launch(Dispatchers.Main) {
                viewmodel.insertUser(ModelUserResposne.UserItems
                    (userId = authdb.currentUser?.uid,name = text.value)).collect {
                    when (it) {
                        is ResultState.Success -> {
                            context.showMsg(
                                msg = it.data
                            )
                            move=true
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
            }}) {
                Text(text = "Submit")
            }
            if (move){
                context.startActivity(Intent(context, MainActivity::class.java))
            }

        }
    }

}