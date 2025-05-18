package com.neesan.data.notification

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import javax.inject.Inject
import androidx.core.content.edit

class NotificationPermissionDao @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Android 12以下では不要
            true
        }
    }

    /**
     * すでに通知許可のリクエストを行ったかどうかを保存するためのメソッド
     **/
    fun setNotificationPermissionRequested() {
        sharedPreferences.edit {
            putBoolean(NOTIFICATION_PERMISSION_REQUESTED_KEY, true)
        }
    }

    /**
     * すでに通知許可のリクエストを行ったかどうかを取得するためのメソッド
     */
    fun isNotificationPermissionRequested(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_PERMISSION_REQUESTED_KEY, false)
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUESTED_KEY =
            "notification_permission_requested"
    }
}