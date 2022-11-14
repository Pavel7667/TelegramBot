package com.spring.telegram.SpringTelegramBot.service;

import com.spring.telegram.SpringTelegramBot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 * TelegramBot class for taking from chats txt request, and hive for every User
 * response by chatID.
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    BotConfig botConfig; // create aggregation


    /**
     * The getBotUsername setting for Bot name
     *
     * @return Bot name
     */
    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    /**
     * The getBotToken setting for Bot token
     *
     * @return Bot token
     */
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    /**
     * The onUpdateReceived in case user send txt message, send response.
     * For "/start" request get userName and extra text, for another send
     * default case.
     *
     * @param update User message
     */
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage()
                            .getChat()
                            .getFirstName());
                    break;
                default:
                    sendMessage(chatId, "Sorry unknown command");
            }
        }
    }

    /**
     * The startCommandReceived method for getting from chatID userName
     *
     * @param chatID chat ID
     * @param name   looking name
     */
    private void startCommandReceived(long chatID, String name) {
        String answer = "Hi userName " + name + " ,this is TelegramBot";
        sendMessage(chatID, answer);
    }

    /**
     * The sendMessage method send message in Telegrams chats. Every chat
     * have special ID
     *
     * @param chatID     chat ID
     * @param textToSend text which will be sending
     */
    private void sendMessage(long chatID, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatID));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
