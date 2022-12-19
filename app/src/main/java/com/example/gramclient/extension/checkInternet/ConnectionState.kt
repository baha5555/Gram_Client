package com.example.gramclient.extension.checkInternet

sealed class ConnectionState{
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}