package com.gram.client.presentation.screens.drawer.messageScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageViewModel:ViewModel() {
    val messages = MutableLiveData<List<String>>()
    private val _messages = mutableListOf<String>()

    fun sendMessage(message:String){
        if(message.isNotEmpty()) {
            _messages.add(message)
            messages.value = _messages.toList()
        }
    }
}