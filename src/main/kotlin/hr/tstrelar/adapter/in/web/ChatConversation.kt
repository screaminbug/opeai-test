package hr.tstrelar.adapter.`in`.web

import hr.tstrelar.application.ports.`in`.ChatCommand
import hr.tstrelar.application.ports.`in`.ConvHistoryQuery
import hr.tstrelar.application.ports.`in`.SaveAssistantMessageCommand
import hr.tstrelar.di.chatModule
import io.ktor.server.sessions.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.reduce
import kotlinx.html.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.chatConverse() {
    data class ChatSession(val id: Int? = null)
    val chatCommand by inject<ChatCommand>()
    val saveAssistantMessageCommand by inject<SaveAssistantMessageCommand>()
    val convHistoryQuery: ConvHistoryQuery by inject()

    install(Koin) {
        slf4jLogger()
        modules(chatModule)
    }
    install(Sessions) {
        cookie<ChatSession>("CHAT_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    routing {
        get("/converse/{message}") {
            val session = call.sessions.get() ?: ChatSession()
            val message = call.parameters["message"] ?: "I didn't say anything"
            val response = chatCommand.processNextMessage(message, session.id)
            val actualConvId = response.first
            call.sessions.set(ChatSession(actualConvId))
            val responseToUser = response.second
            saveAssistantMessageCommand.save(responseToUser, actualConvId)
            call.respondText(responseToUser.reduce { s, t -> s + t })
        }
        get("/conversation-history") {
            val session = call.sessions.get() ?: ChatSession()
            val convId = session.id
            val response = convHistoryQuery.fetchFor(convId)
            call.respondHtml {
                body {
                    table {
                        tr {
                            th { +"Time" }
                            th { +"Participant" }
                            th { +"Message" }
                        }
                        response.forEach {
                            tr {
                                td { + it.time }
                                td { + it.participant }
                                td { + it.message }
                            }
                        }
                    }
                }
            }
        }
    }
}
