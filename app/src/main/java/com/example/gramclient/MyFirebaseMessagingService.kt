package com.example.gramclient

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.gramclient.Constants.FCM_TOKEN
import com.example.gramclient.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
const val chanel_name="com.gram.gramclient"
const val chanel_id="notification_channel"
class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val id = Regex("[0-9]+")
//        messageOrderId = id.find(remoteMessage.notification!!.body!!)?.value
//        if (messageOrderId != null) {
//            Log.e("MessageOrderId", " mes -> $messageOrderId")
//            stateMessage = true
//        }
        if (remoteMessage.notification != null) {
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
            Log.v("CloudMessage", "Notification ${remoteMessage.notification!!}")
            Log.v("CloudMessage", "Notification Title ${remoteMessage.notification!!.title}")
            Log.v("CloudMessage", "Notification Body ${remoteMessage.notification!!.body}")
        }
    }
    fun getRemoteView(title: String, body:String): RemoteViews {
        val remoteView= RemoteViews("com.gram.gramclient", R.layout.notification)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, body)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.ic_logo_gram)
        return remoteView
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title:String, body:String){
        val intent= Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder=
            NotificationCompat.Builder(applicationContext, chanel_id)
                .setSmallIcon(R.drawable.ic_logo_gram)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingintent)
        builder=builder.setContent(getRemoteView(title, body))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel=
                NotificationChannel(chanel_id, chanel_name, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCM_TOKENN","-> $token")
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful){
                return@addOnCompleteListener
            }
            val token=task.result
            FCM_TOKEN = task.result
            Log.e("Hello", "FCM-Token-> \n $token")}
    }
}