package hr.tstrelar.application.ports.`in`

interface ConvHistoryQuery {
    fun fetchFor(conversationId: Int?): List<ConvHistoryResponse>
}