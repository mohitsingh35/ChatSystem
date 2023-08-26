package com.ncs.chatsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.ncs.chatsystem.ui.theme.AuthScreenotp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpLoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthScreenotp(activity = this)
        }
    }
}