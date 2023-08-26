package com.ncs.chatsystem.firebaseauth.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.ncs.chatsystem.firebaseauth.repository.AuthRepository
import com.ncs.guessr.utils.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authdb:FirebaseAuth
): AuthRepository {
    private lateinit var onVerificationCode:String


    override fun createUserWithPhone(phone: String,activity: Activity): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val onVerificationCallback = object :
            PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {


            }

            override fun onVerificationFailed(p0: FirebaseException) {
                trySend(ResultState.Failure(p0))
            }

            override fun onCodeSent(verificationCode: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationCode, p1)
                trySend(ResultState.Success("OTP sent successfully"))
                onVerificationCode=verificationCode
            }
        }
        val options=PhoneAuthOptions.newBuilder(authdb)
            .setPhoneNumber("+91$phone")
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(onVerificationCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose{
            close()
        }
    }

    override fun signInwithotp(otp: String): Flow<ResultState<String>> = callbackFlow{
       trySend(ResultState.Loading)
        val credential= PhoneAuthProvider.getCredential(onVerificationCode,otp)
        authdb.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    trySend(ResultState.Success("OTP Verified"))

                }
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose{
            close()
        }
    }

}