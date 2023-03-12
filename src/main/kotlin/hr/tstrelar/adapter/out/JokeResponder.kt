package hr.tstrelar.adapter.out

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import hr.tstrelar.application.ports.out.CompleteChatPort
import hr.tstrelar.domain.Message
import hr.tstrelar.domain.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class JokeResponder(
    private val mapper: OpenAiChatMessageMapper,
    private val openAi: OpenAI,
    private val model: ModelId
) : CompleteChatPort {

    @OptIn(BetaOpenAI::class)
    override fun completeChat(messages: List<Message>): Flow<String> {
        val preamble = listOf(
            Message(Role.USER, "From now on, you will respond to every single message with a dry and inappropriate joke. Say OK if you agree"),
            Message(Role.ASSISTANT, "OK")
        )
        val preparedMessages = (preamble + messages).map {
            mapper.toOpenAiChatMessage(it)
        }
        val request = ChatCompletionRequest(model, preparedMessages)
        val completion = openAi.chatCompletions(request)
        return completion.map {
            it.choices.first().delta?.content ?: ""
        }
    }
}