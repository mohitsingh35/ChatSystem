package com.ncs.chatsystem.firebaseauth.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ncs.guessr.utils.ResultState

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeDBRepository @Inject constructor(
    private val db:DatabaseReference
):RealtimeRepository {
    private var authDb=Firebase.auth
    private var storageReference=Firebase.storage
    private val userid=authDb.currentUser?.uid
    var chatList= ArrayList<RealTimeModelResponse.RealTimeItems>()
    override fun insertMessage(item: RealTimeModelResponse.RealTimeItems): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            db.child("messages").push().setValue(
                item
            ).addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(ResultState.Success("Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
            awaitClose {
                close()
            }
        }

    override fun insertuser(item: ModelUserResposne.UserItems): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            db.child("users").push().setValue(
                item
            ).addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(ResultState.Success("Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
            awaitClose {
                close()
            }
        }

    override fun getMessage(senderId:String,receiverId:String): Flow<ResultState<List<RealTimeModelResponse>>> = callbackFlow{
        trySend(ResultState.Loading)

        val valueEvent=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val items=snapshot.children.map {
                    RealTimeModelResponse(
                        it.getValue(RealTimeModelResponse.RealTimeItems::class.java),
                        key = it.key
                    )
                }
                val chatList= ArrayList<RealTimeModelResponse>()
                chatList.clear()
                for (i in 0 until items.size){
                    chatList.add(items[i])
                }
                trySend(ResultState.Success(chatList))

            }


            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }

        }
        db.child("messages").addValueEventListener(valueEvent)
        awaitClose{
            db.child("messages").removeEventListener(valueEvent)
            close()
        }
    }

    override fun getUser(): Flow<ResultState<List<ModelUserResposne>>> = callbackFlow{
        trySend(ResultState.Loading)

        val valueEvent=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val items=snapshot.children.map {
                    ModelUserResposne(
                        it.getValue(ModelUserResposne.UserItems::class.java),
                        key = it.key
                    )
                }
                trySend(ResultState.Success(items))
                Log.d("mohit",items.toString())
            }


            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }

        }
        db.child("users").addValueEventListener(valueEvent)
        awaitClose{
            db.child("users").removeEventListener(valueEvent)
            close()
        }
    }

}