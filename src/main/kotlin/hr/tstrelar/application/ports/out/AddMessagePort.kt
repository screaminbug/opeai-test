package hr.tstrelar.application.ports.out

import kotlinx.coroutines.flow.Flow

interface AddMessagePort {
    fun addUserMessage(message: String, conversationId: Int? = null): Int
    suspend fun addAssistantMessage(messageFlow: Flow<String>, conversationId: Int? = null)
}