package hr.tstrelar.application.ports.out

import hr.tstrelar.domain.Message
import kotlinx.coroutines.flow.Flow

interface CompleteChatPort {
    fun completeChat(messages: List<Message>): Flow<String>
}