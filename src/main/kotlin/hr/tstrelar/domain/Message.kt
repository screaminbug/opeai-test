package hr.tstrelar.domain

import java.time.LocalDateTime

data class Message(
    val role: Role,
    val contents: String,
    val name: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
