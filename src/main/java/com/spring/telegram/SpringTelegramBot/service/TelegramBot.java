package com.spring.telegram.SpringTelegramBot.service;

import com.spring.telegram.SpringTelegramBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


/**
 * TelegramBot class for taking from chats txt request, and hive for every User
 * response by chatID.
 */
@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {


    final BotConfig botConfig; // create aggregation

    final private String helpText = "Just write needed Text";

    /**
     * Constructor for creating bot
     * Also added a menu function with commands
     *
     * @param botConfig with INFO(name,token) from properties file
     */
    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", "Welcome message"));
        botCommandList.add(new BotCommand("/myData", "User Data"));
        botCommandList.add(new BotCommand("/deleteData", "Delete Data"));
        botCommandList.add(new BotCommand("/help", "Instruction"));
        botCommandList.add(new BotCommand("/settings", "Settings"));
        try {
            this.execute(new SetMyCommands(botCommandList,
                    new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {
            log.error("Error setting bot`s command list: " + e.getMessage());
        }
    }


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
                case "/help":
                    startCommandReceived(chatId, helpText);
                    break;
                default:
                    sendMessage(chatId, "Sorry unknown command");
            }
        }
    }

    /**
     * The startCommandReceived method for getting from chatID userName
     * And save in Log File, Users who were came
     *
     * @param chatID chat ID
     * @param name   looking name
     */
    private void startCommandReceived(long chatID, String name) {
        String answer = "Hi user name is = " + name;
        log.info("User by name come in: !!! = " + name);
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
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
