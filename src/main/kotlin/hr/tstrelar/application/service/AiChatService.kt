package hr.tstrelar.application.service

import hr.tstrelar.application.ports.`in`.ChatCommand
import hr.tstrelar.application.ports.`in`.ConvHistoryQuery
import hr.tstrelar.application.ports.`in`.ConvHistoryResponse
import hr.tstrelar.application.ports.`in`.SaveAssistantMessageCommand
import hr.tstrelar.application.ports.out.AddMessagePort
import hr.tstrelar.application.ports.out.CompleteChatPort
import hr.tstrelar.application.ports.out.LoadMessagesPort
import kotlinx.coroutines.flow.Flow

class AiChatService(
    private val chatCompletion: CompleteChatPort,
    private val messageAdder: AddMessagePort,
    private val messageLoader: LoadMessagesPort,
    private val messageMapper: MessageHistoryMapper
) : ChatCommand, SaveAssistantMessageCommand, ConvHistoryQuery {
    override fun processNextMessage(message: String, conversationId: Int?): Pair<Int, Flow<String>> {
        val actualConvId = messageAdder.addUserMessage(message, conversationId)
        val messages = messageLoader.loadForConversation(actualConvId)
        val result = chatCompletion.completeChat(messages)

        return Pair(actualConvId, result)
    }

    override suspend fun save(message: Flow<String>, messageId: Int) {
        messageAdder.addAssistantMessage(message, messageId)
    }

    override fun fetchFor(conversationId: Int?): List<ConvHistoryResponse> {
        val messages = conversationId?.let { messageLoader.loadForConversation(it) } ?: listOf()
        return messages.map(messageMapper::toMessageHistoryResponse)
    }
}