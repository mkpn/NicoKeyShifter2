package com.neesan.domain.notification

import com.neesan.data.notification.NotificationPermissionRepository
import javax.inject.Inject

class UpdateNotificationPermissionRequestedUseCase @Inject constructor(
    private val notificationPermissionRepository: NotificationPermissionRepository
) {
    fun invoke() =
        notificationPermissionRepository.updateNotificationPermissionRequested()
} 