package hr.tstrelar.adapter.out

import hr.tstrelar.application.ports.out.AddMessagePort
import hr.tstrelar.application.ports.out.LoadMessagesPort
import hr.tstrelar.domain.Message
import hr.tstrelar.domain.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.reduce
import java.util.concurrent.atomic.AtomicInteger

class MessageRepository : AddMessagePort, LoadMessagesPort {

    private val counter = AtomicInteger(0)
    private val messageRepo = mutableMapOf<Int, List<Message>>()
    override fun loadForConversation(conversationId: Int) = messageRepo[conversationId] ?: listOf()
    override fun addUserMessage(message: String, conversationId: Int?) =
        addMessage(contents = message, role = Role.USER, id = conversationId)
    override suspend fun addAssistantMessage(messageFlow: Flow<String>, conversationId: Int?) {
        val message = messageFlow.reduce { k, s -> k + s }
        addMessage(contents = message, role = Role.ASSISTANT, id = conversationId)
    }
    private fun addMessage(contents: String, role: Role, id: Int?): Int {
        val existingChat = messageRepo[id]
        val message = Message(role, contents)
        val updatedChat = existingChat?.let { it + listOf(message) } ?: listOf(message)
        val key = id ?: counter.addAndGet(1)
        messageRepo[key] = updatedChat
        return key
    }
}