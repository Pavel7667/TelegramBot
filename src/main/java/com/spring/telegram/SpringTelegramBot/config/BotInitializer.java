package com.spring.telegram.SpringTelegramBot.config;

import com.spring.telegram.SpringTelegramBot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Class for activating Bot
 */
@Component
public class BotInitializer {

    @Autowired
    TelegramBot bot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi =
                new TelegramBotsApi(DefaultBotSession.class);
        try{
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e){
            new RuntimeException(e);
        }
    }


}
