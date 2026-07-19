package com.app.sanad.notifications.data.entities

data class Notification(
    val title: String ?= null,
    val body: String ?= null,
    val bodyAr: String ?= null,
    val bodyEn: String ?= null,
    val type:String ?= null,
    val imageSender:String ?= null,
    val time: Long ?= System.currentTimeMillis(),
    val read:Boolean ?= false,
    val id:String =  java.util.UUID.randomUUID().toString(),
    )

enum class NotificationsEnum(){
    System,
    Coffee,
    Chat,
    Sharing
}