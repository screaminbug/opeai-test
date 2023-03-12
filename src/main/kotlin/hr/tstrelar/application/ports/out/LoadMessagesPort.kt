package hr.tstrelar.application.ports.out

import hr.tstrelar.domain.Message

interface LoadMessagesPort {
    fun loadForConversation(conversationId: Int): List<Message>
}