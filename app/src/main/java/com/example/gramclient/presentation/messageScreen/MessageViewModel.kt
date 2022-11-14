package com.example.gramclient.presentation.messageScreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

class MessageViewModel:ViewModel() {
    val messages = MutableLiveData<List<String>>()
    val _messages = mutableListOf<String>()

    fun sendMessage(message:String){
        if(message.isNotEmpty()) {
            _messages.add(message)
            messages.value = _messages.toList()
        }
    }
}