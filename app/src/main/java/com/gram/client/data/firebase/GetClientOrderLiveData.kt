package com.gram.client.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.gram.client.domain.firebase.profile.Client
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GetClientOrderLiveData(val client:String): LiveData<Client>() {
    private val  database = Firebase.database.reference
        .child("clients/$client")
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try{
                value = snapshot.getValue(Client::class.java)
                Log.e("token Client", "$value")
            } catch (_: Exception){
                val l = listOf(-1)
                value = Client(active_orders = l)
            }
        }
        override fun onCancelled(error: DatabaseError) {
            Log.e("RealtimeClientOrderIdDatabaseResponse","ERROR_CANCELLED")
        }
    }

    override fun onActive() {
        database.addValueEventListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        database.removeEventListener(listener)
        super.onInactive()
    }
}