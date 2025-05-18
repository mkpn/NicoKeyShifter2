package com.neesan.nicokeyshifter2.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.neesan.nicokeyshifter2.MainActivity
import com.neesan.nicokeyshifter2.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NicoKeyShifterMessagingService : FirebaseMessagingService() {

    /**
     * FCMトークンが生成または更新されたときに呼び出されます。
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // トークンをサーバーに送信する必要がある場合は、ここで実装します
        // 例: sendRegistrationToServer(token)
    }

    /**
     * メッセージを受信したときに呼び出されます。
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // データペイロードを処理
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            
            // 長時間実行タスクが必要な場合はWorkManagerを使用
            // scheduleJob()
        }

        // 通知ペイロードを処理
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.title, it.body)
        }
    }

    /**
     * 通知を作成して表示します。
     */
    private fun sendNotification(title: String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title ?: "NicoKeyShifter2")
            .setContentText(messageBody ?: "新しい通知があります")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android Oreo以降では通知チャンネルが必要
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "NicoKeyShifter2通知",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "NicoKeyShifterFCM"
    }
}
