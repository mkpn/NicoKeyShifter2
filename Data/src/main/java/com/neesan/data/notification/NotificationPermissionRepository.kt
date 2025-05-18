package com.neesan.data.notification

import javax.inject.Inject

class NotificationPermissionRepository @Inject constructor(
    private val notificationPermissionDao: NotificationPermissionDao
) {

    fun hasNotificationPermission(): Boolean {
        return notificationPermissionDao.hasNotificationPermission()
    }

    fun updateNotificationPermissionRequested() {
        notificationPermissionDao.setNotificationPermissionRequested()
    }
}