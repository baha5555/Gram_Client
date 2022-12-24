package com.example.gramclient.domain.realtimeDatabase

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.firebaserealtimedatabase.orders.Allowance
import com.example.firebaserealtimedatabase.orders.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AllNotesLiveData: LiveData<List<Order>>() {
    private lateinit var allowancesListener:LiveData<List<Allowance>>
    private val mAuth = FirebaseAuth.getInstance()
    private val  database = Firebase.database.reference
        .child("orders")
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val order = mutableListOf<Allowance>()
            val notes = mutableListOf<Order>()
            snapshot.children.map { it ->
                Log.e("auth token", "$it<-")
                notes.add(it.getValue(Order::class.java) ?: Order())
            }
            value = notes
        }
        override fun onCancelled(error: DatabaseError) {

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