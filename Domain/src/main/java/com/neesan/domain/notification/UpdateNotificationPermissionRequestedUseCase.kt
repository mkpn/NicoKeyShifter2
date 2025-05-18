package com.neesan.domain.notification

import com.neesan.data.notification.NotificationPermissionRepository
import javax.inject.Inject

/**
 * 通知の権限をリクエスト済みの状態に更新するUseCase
 */
class UpdateNotificationPermissionRequestedUseCase @Inject constructor(
    private val notificationPermissionRepository: NotificationPermissionRepository
) {
    fun invoke() =
        notificationPermissionRepository.updateNotificationPermissionRequested()
} 