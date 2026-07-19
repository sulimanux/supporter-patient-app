package com.app.sanad.chatting.data.entity

data class Chatting(
    val meta: MetaDataMessages? = null,
    var messages: MutableList<Message>? = mutableListOf()
)