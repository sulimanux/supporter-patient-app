package com.app.sanad.notifications.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.app.sanad.notifications.data.repos.NotificationsRepo
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repo: NotificationsRepo
) : ViewModel() {

    val notifications = repo.notifications

    fun stop() {
        repo.removeNotificationsListener()
    }

    fun delete(userId: String, id: String) {
        repo.deleteNotification(userId, id)
    }

    fun clear(userId: String) {
        repo.clearNotifications(userId)
    }

    fun markAsRead(id: String) {
        repo.markNotificationAsRead(id)
    }

    fun markAllAsRead() {
        repo.markAllNotificationsAsRead()
    }
}