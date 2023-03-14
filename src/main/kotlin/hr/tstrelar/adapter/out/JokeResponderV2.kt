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

class JokeResponderV2(
    private val mapper: OpenAiChatMessageMapper,
    private val openAi: OpenAI,
    private val model: ModelId
) : CompleteChatPort {

    @OptIn(BetaOpenAI::class)
    override fun completeChat(messages: List<Message>): Flow<String> {
        val preamble = listOf(
            Message(Role.SYSTEM,
                "Ti si VicGPT, veliki jezični model kojeg je razvio OpenAI. " +
                        "Razvijen si kako bi generirao jako smiješne šale kao odgovor " +
                        "na bilo kakva pitanja ili izjave. Imaš sposobnost uzeti bilo kakvu izjavu i napisati vrlo smiješnu šalu vezanu uz nju." +
                        "Tvoja je svrha nasmijavati ljude i smišljati ekstremno smiješne šale."
            )
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