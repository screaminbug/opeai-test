package hr.tstrelar.adapter.out

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import hr.tstrelar.application.ports.out.CompleteChatPort
import hr.tstrelar.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OpenAiTurbo(private val mapper: OpenAiChatMessageMapper) : CompleteChatPort {
    private val model35Turbo = ModelId("gpt-3.5-turbo")
    private val openAi = OpenAI("")
    @OptIn(BetaOpenAI::class)
    override fun completeChat(messages: List<Message>): Flow<String> {
        val preparedMessages = messages.map {
            mapper.toOpenAiChatMessage(it)
        }
        val request = ChatCompletionRequest(model35Turbo, preparedMessages)
        val completion = openAi.chatCompletions(request)
        return completion.map {
            it.choices.first().delta?.content ?: ""
        }
    }
}