package com.gram.client.app.preference

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.gram.client.utils.PreferencesName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomPreference @Inject constructor(@ApplicationContext context : Context){
    private val prefs:SharedPreferences = context.getSharedPreferences(PreferencesName.APP_PREFERENCES, Context.MODE_PRIVATE)
    fun clearPreference() {
        prefs.edit().clear().apply()
    }
    fun getAccessToken(): String {
        return prefs.getString(PreferencesName.ACCESS_TOKEN, "")!!
    }
    fun setAccessToken(query: String) {
        prefs.edit().putString(PreferencesName.ACCESS_TOKEN, query).apply()
    }
    fun getSocketAccessToken(): String {
        return prefs.getString(PreferencesName.SOCKET_ACCESS_TOKEN, "")!!
    }
    fun setSocketAccessToken(query: String) {
        prefs.edit().putString(PreferencesName.SOCKET_ACCESS_TOKEN, query).apply()
    }
    fun getFcmToken(): String {
        return prefs.getString(PreferencesName.FCM_TOKEN, "")!!
    }
    fun setFcmToken(query: String) {
        prefs.edit().putString(PreferencesName.FCM_TOKEN, query).apply()
    }
    fun getPhoneNumber(): String {
        return prefs.getString(PreferencesName.PHONE_NUMBER, "")!!
    }
    fun setPhoneNumber(query: String) {
        prefs.edit().putString(PreferencesName.PHONE_NUMBER, "992$query").apply()
    }
}
