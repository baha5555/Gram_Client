package com.gram.client.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.gram.client.R
import com.gram.client.utils.Constants.FCM_TOKEN
import com.gram.client.presentation.MainActivity
import com.gram.client.utils.Constants.STATE_ASSIGNED_ORDER
import com.gram.client.utils.Constants.STATE_ASSIGNED_ORDER_ID
import com.gram.client.utils.Constants.STATE_DRIVER_IN_SITE
import com.gram.client.utils.Constants.STATE_DRIVER_IN_SITE_ORDER_ID
import com.gram.client.utils.Constants.STATE_RATING
import com.gram.client.utils.Constants.STATE_RAITING_ORDER_ID
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
const val chanel_name="com.gram.gramclient"
const val chanel_id="notification_channel"
class MyFirebaseMessagingService: FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            Handler(Looper.getMainLooper()).post {
                when(remoteMessage.data["type"]) {
                    "0" ->{
                        STATE_ASSIGNED_ORDER.value = true
                        STATE_ASSIGNED_ORDER_ID.value = remoteMessage.data["order_id"]?.toInt()?:-1
                    }
                    "1"->{
                        STATE_DRIVER_IN_SITE.value = true
                        STATE_DRIVER_IN_SITE_ORDER_ID.value = remoteMessage.data["order_id"]?.toInt()?:-1
                    }
                    "2" -> {
                        STATE_RATING.value = remoteMessage.notification!!.title != ""
                        STATE_RAITING_ORDER_ID.value = remoteMessage.data["order_id"]?.toInt() ?: -1
                    }
                }
            }
            Log.e("CloudMessage","${remoteMessage.data} ---- ")
            generateNotification(remoteMessage.notification!!.title?:"", remoteMessage.notification!!.body?:"")
            Log.e("CloudMessage", "Notification Title ${remoteMessage.notification!!.title}")
            Log.e("CloudMessage", "Notification Body ${remoteMessage.notification!!.body}")
        }
    }
    fun getRemoteView(title: String, body:String): RemoteViews {
        val remoteView= RemoteViews("com.gram.client", R.layout.notification)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, body)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.notification_logo_gram)
        return remoteView
    }
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title:String, body:String){
        val intent= Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        var builder: NotificationCompat.Builder=
            NotificationCompat.Builder(applicationContext, chanel_id)
                .setSmallIcon(R.drawable.notification_logo_gram)
                .setVibrate(longArrayOf(2000, 2000, 2000, 2000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
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