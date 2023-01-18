package com.example.gramclient.app.extension.checkInternet

sealed class ConnectionState{
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}