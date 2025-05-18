package com.neesan.domain.notification

import com.neesan.data.notification.NotificationPermissionRepository
import javax.inject.Inject

class CheckNotificationPermissionUseCase @Inject constructor(
    private val notificationPermissionRepository: NotificationPermissionRepository
) {
    fun invoke() =
        notificationPermissionRepository.hasNotificationPermission()
} 