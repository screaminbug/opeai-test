package hr.tstrelar.application.ports.`in`

import kotlinx.coroutines.flow.Flow

interface ChatCommand {
    fun processNextMessage(message: String, conversationId: Int? = null): Pair<Int, Flow<String>>
}