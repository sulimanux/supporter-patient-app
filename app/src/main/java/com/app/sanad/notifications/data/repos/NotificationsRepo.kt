package com.app.sanad.notifications.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.app.sanad.notifications.data.entities.Notification
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log

class NotificationsRepo(
    private val db: FirebaseFirestore,
    private val sharedPreferences: SharedPreferencesManager
) {

    private var listener: ListenerRegistration? = null
    private val userId = sharedPreferences.getUserProfile().id!!
    val unreadCountNotification = MutableLiveData<Int>()
    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications:LiveData<List<Notification>> = _notifications

    fun setNotificationsListener( ) {

        listener = db.collection("notifications")
            .document(userId)
            .collection("user_notifications")
            .addSnapshotListener { value, _ ->
                _notifications.value = value?.map { it.toObject(Notification::class.java) } ?: emptyList()
                unreadCountNotification.value = _notifications.value?.filter{!it.read!!}?.size
                log("AppViewModel setNotificationsListener ${userId} ${_notifications.value?.filter{!it.read!!}?.size}")
            }
    }

    fun removeNotificationsListener() {
        listener?.remove()
        listener = null
    }


    fun sendNotification(userId: String , notification: Notification){
        db.collection("notifications")
            .document(userId)
            .collection("user_notifications")
            .document(notification.id)
            .set(notification)
    }

    fun deleteNotification(userId: String, notificationId: String) {
        db.collection("notifications")
            .document(userId)
            .collection("user_notifications")
            .document(notificationId)
            .delete()
    }

    fun clearNotifications(userId: String) {
        db.collection("notifications")
            .document(userId)
            .collection("user_notifications")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    doc.reference.delete()
                }
            }
    }


    fun markNotificationAsRead(notificationId: String) {
        db.collection("notifications")
            .document(userId)
            .collection("user_notifications")
            .document(notificationId)
            .update("read", true)
    }

    fun markAllNotificationsAsRead() {
        db.collection("notifications")
            .document(userId)
            .collection("user_notifications")
            .whereEqualTo("read", false)
            .get()
            .addOnSuccessListener { result ->
                val batch = db.batch()
                for (doc in result) {
                    batch.update(doc.reference, "read", true)
                }
                batch.commit()
            }
    }

    fun getNotifications(userId: String, onResult: (List<Notification>) -> Unit) {
        db.collection("notifications")
            .document(userId)
            .collection("user_notifications")
            .get()
            .addOnSuccessListener { result ->
                val list = result.map { it.toObject(Notification::class.java) }
                onResult(list)
            }
    }
}