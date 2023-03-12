package hr.tstrelar.adapter.`in`.web

import hr.tstrelar.application.ports.`in`.ChatCommand
import hr.tstrelar.application.ports.`in`.ConvHistoryQuery
import hr.tstrelar.application.ports.`in`.ConvHistoryResponse
import hr.tstrelar.application.ports.`in`.SaveAssistantMessageCommand
import hr.tstrelar.di.chatModule
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import kotlin.collections.List
import kotlin.collections.forEach
import kotlin.collections.set

fun Application.chatConverse() {
    data class ChatSession(val id: Int? = null)
    val chatCommand by inject<ChatCommand>()
    val saveAssistantMessageCommand by inject<SaveAssistantMessageCommand>()
    val convHistoryQuery: ConvHistoryQuery by inject()

    install(Koin) {
        slf4jLogger()
        modules(chatModule)
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    install(Sessions) {
        cookie<ChatSession>("CHAT_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    routing {
        route("/") {

            var result: List<ConvHistoryResponse>? = null

            post("/submit") {
                val session = call.sessions.get() ?: ChatSession()
                val message = call.receiveParameters()["message"] ?: "I didn't say anything"
                val response = chatCommand.processNextMessage(message, session.id)
                val actualConvId = response.first
                call.sessions.set(ChatSession(actualConvId))
                val responseToUser = response.second
                saveAssistantMessageCommand.save(responseToUser, actualConvId)
                result = convHistoryQuery.fetchFor(actualConvId)
                call.respondRedirect("/")
            }
            get("/") {
                call.respondHtml {
                    body {
                        h1 { +"Chat away..." }
                        table {
                            tr {
                                th { +"Time" }
                                th { +"Participant" }
                                th { +"Message" }
                            }
                            result?.forEach {
                                tr {
                                    td { + it.time }
                                    td { + it.participant }
                                    td { + it.message }
                                }
                            }
                        }
                        form(action = "/submit", method = FormMethod.post) {
                            textArea {
                                name = "message"
                            }
                            br
                            submitInput {
                                value = "Submit"
                            }
                        }
                    }
                }
            }

        }
    }
}
