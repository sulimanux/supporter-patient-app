package com.app.sanad.auth.data.entity

data class Partner(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val imageUser: String? = null,
    val currentDay:Int? = null,
)

{


    override fun toString(): String {
        return "Partner(id=$id, name=$name, email=$email, currentDay=$currentDay , imageUser=$imageUser)"
    }
}