package com.example.gramclient.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.gramclient.domain.realtimeDatabase.Order.RealtimeDatabaseOrder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AllOrdersLiveData: LiveData<List<RealtimeDatabaseOrder>>() {
    private val  database = Firebase.database.reference
        .child("orders")
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val notes = mutableListOf<RealtimeDatabaseOrder>()
            snapshot.children.map { it ->
                Log.e("token Order", "$it<-")
                notes.add(it.getValue(RealtimeDatabaseOrder::class.java) ?: RealtimeDatabaseOrder())
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