package hr.tstrelar.application.ports.`in`

import kotlinx.coroutines.flow.Flow

interface SaveAssistantMessageCommand {
    suspend fun save(message: Flow<String>, messageId: Int)
}