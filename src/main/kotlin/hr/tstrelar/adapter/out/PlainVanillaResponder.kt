package hr.tstrelar.adapter.out

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import hr.tstrelar.application.ports.out.CompleteChatPort
import hr.tstrelar.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlainVanillaResponder(
    private val mapper: OpenAiChatMessageMapper,
    private val openAi: OpenAI,
    private val model: ModelId
) : CompleteChatPort {
    @OptIn(BetaOpenAI::class)
    override fun completeChat(messages: List<Message>): Flow<String> {
        val preparedMessages = messages.map {
            mapper.toOpenAiChatMessage(it)
        }
        val request = ChatCompletionRequest(model, preparedMessages)
        val completion = openAi.chatCompletions(request)
        return completion.map {
            it.choices.first().delta?.content ?: ""
        }
    }
}