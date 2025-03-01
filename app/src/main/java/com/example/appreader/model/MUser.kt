package com.example.appreader.model

data class MUser(
    val id: String?,
    val userId: String,
    val displayName: String,
    val avatarURL: String,
    val quote: String,
    val profession: String
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "user_id" to userId,
            "display_name" to displayName,
            "avatar_url" to avatarURL,
            "quote" to quote,
            "profession" to profession
        )
    }
}