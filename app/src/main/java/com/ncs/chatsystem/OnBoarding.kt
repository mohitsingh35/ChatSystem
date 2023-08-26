package com.ncs.chatsystem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


class OnBoarding: ComponentActivity() {
    val PREF_NAME = "user"
    val KEY_VARIABLE = "uid"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val context= LocalContext.current
            val pref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val uid = pref.getString(KEY_VARIABLE,null)
            if (uid==null){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Button(onClick = { context.startActivity(Intent(context, OtpLoginActivity::class.java)) }) {
                        Text(text = "Get Started")
                    }
                }
            }
            else{
                context.startActivity(Intent(context, MainActivity::class.java))
            }


        }
    }
}