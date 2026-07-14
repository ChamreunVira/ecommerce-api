//package com.kh.vira_dev.ecommerceapi.bot;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
//import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
//import org.telegram.telegrambots.meta.generics.TelegramClient;
//
//@Component
//@RequiredArgsConstructor
//public class TelegramBot implements SpringLongPollingBot {
//
////    private final TelegramClient telegramClient;
//
//    @Value("${telegram.bot.token}")
//    private String botToken;
//
//    @Override
//    public String getBotToken() {
//        return botToken;
//    }
//
//    @Override
//    public LongPollingUpdateConsumer getUpdatesConsumer() {
//        return null;
//    }
//}
