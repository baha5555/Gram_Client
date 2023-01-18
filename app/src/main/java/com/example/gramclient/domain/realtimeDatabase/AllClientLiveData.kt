package com.example.gramclient.domain.realtimeDatabase

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import com.example.gramclient.domain.realtimeDatabase.profile.Client
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AllClientLiveData(val client:String): LiveData<Client>() {
    private val mAuth = FirebaseAuth.getInstance()
    private val  database = Firebase.database.reference
        .child("clients/$client")
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.getValue(Client::class.java)
            Log.e("token Client","$value")
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