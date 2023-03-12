package hr.tstrelar.application.service

import hr.tstrelar.application.ports.`in`.ConvHistoryResponse
import hr.tstrelar.domain.Message
import java.time.format.DateTimeFormatter

class MessageHistoryMapper {

    val CUSTOM_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    fun toMessageHistoryResponse(message: Message) = ConvHistoryResponse(
        participant = message.role.name,
        message = message.contents,
        time = CUSTOM_FORMATTER.format(message.timestamp)
    )
}