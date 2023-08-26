package com.ncs.chatsystem

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.ncs.chatsystem.ui.theme.ChatSystemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var authdb: FirebaseAuth
    val PREF_NAME = "user"
    val KEY_VARIABLE = "uid"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authdb = FirebaseAuth.getInstance()

        setContent {
            ChatSystemTheme {
                val pref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                val uid=authdb.currentUser?.uid
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putString(KEY_VARIABLE, uid)
                editor.apply()
                mainScreen()

            }
        }
    }
}

