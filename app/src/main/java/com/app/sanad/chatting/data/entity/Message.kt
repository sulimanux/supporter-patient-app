package com.app.sanad.chatting.data.entity

data class Message(
    val text: String? = null,
    val senderId: String? = null,
    val timeStamp: Long? = System.currentTimeMillis(),
    var seenBySupporter: Boolean = false,
    var seenByPatient: Boolean = false,
    )
