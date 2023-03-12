package hr.tstrelar

import hr.tstrelar.adapter.`in`.web.chatConverse
import hr.tstrelar.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    chatConverse()
    configureSerialization()
    configureTemplating()
    configureRouting()

}
