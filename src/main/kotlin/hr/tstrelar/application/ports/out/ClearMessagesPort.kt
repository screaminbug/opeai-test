package hr.tstrelar.application.ports.out

//TODO: Implement this
interface ClearMessagesPort {
    fun clear(conversationId: Int)
    fun clearAll()
}