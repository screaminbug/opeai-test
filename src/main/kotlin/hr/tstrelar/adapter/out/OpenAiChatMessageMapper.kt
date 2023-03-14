package hr.tstrelar.adapter.out

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import hr.tstrelar.domain.Message
import hr.tstrelar.domain.Role

class OpenAiChatMessageMapper {
    @OptIn(BetaOpenAI::class)
    fun toOpenAiChatMessage(message: Message) = ChatMessage(
        role = toOpenAiRole(message.role),
        content = message.contents,
        name = message.name
    )

    @OptIn(BetaOpenAI::class)
    fun toAppMessage(message: ChatMessage) = Message(
        role = toAppRole(message.role),
        contents = message.content,
        name = message.name
    )
    @OptIn(BetaOpenAI::class)
    fun toOpenAiRole(role: Role) = when(role) {
        Role.USER -> ChatRole.User
        Role.ASSISTANT -> ChatRole.Assistant
        Role.SYSTEM -> ChatRole.System
    }

    @OptIn(BetaOpenAI::class)
    fun toAppRole(role: ChatRole) = when(role) {
        ChatRole.User -> Role.USER
        ChatRole.Assistant -> Role.ASSISTANT
        ChatRole.System -> Role.SYSTEM
        else -> throw NotImplementedError("Currently only \"User\", \"Assistant\" and \"System\" roles are supported")
    }
}