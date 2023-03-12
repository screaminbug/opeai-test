package hr.tstrelar.di

import hr.tstrelar.adapter.out.OpenAiTurbo
import hr.tstrelar.adapter.out.MessageRepository
import hr.tstrelar.adapter.out.OpenAiChatMessageMapper
import hr.tstrelar.application.service.AiChatService
import hr.tstrelar.application.service.MessageHistoryMapper
import hr.tstrelar.application.ports.`in`.ChatCommand
import hr.tstrelar.application.ports.`in`.ConvHistoryQuery
import hr.tstrelar.application.ports.`in`.SaveAssistantMessageCommand
import hr.tstrelar.application.ports.out.AddMessagePort
import hr.tstrelar.application.ports.out.CompleteChatPort
import hr.tstrelar.application.ports.out.LoadMessagesPort

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val chatModule = module {
    singleOf(::OpenAiTurbo) { bind<CompleteChatPort>() }
    singleOf(::MessageRepository) {
        bind<AddMessagePort>()
        bind<LoadMessagesPort>()
    }
    singleOf(::AiChatService) {
        bind<ChatCommand>()
        bind<SaveAssistantMessageCommand>()
        bind<ConvHistoryQuery>()
    }
    singleOf(::OpenAiChatMessageMapper)
    singleOf(::MessageHistoryMapper)
}