package com.example.gramclient.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GetOrdersLiveData: LiveData<List<RealtimeDatabaseOrder>>() {
    private val  database = Firebase.database.reference
        .child("orders")
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val notes = mutableListOf<RealtimeDatabaseOrder>()
            value = try {
                snapshot.children.map { it ->
                    Log.e("token Order", "$it<-")
                    notes.add(it.getValue(RealtimeDatabaseOrder::class.java) ?: RealtimeDatabaseOrder())
                }
                notes
            }catch (_: Exception){
                notes
            }
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