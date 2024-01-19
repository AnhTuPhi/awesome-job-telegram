package vn.com.tech.telegram.bot.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Admin
 * @created 19/01/2024 - 9:58 AM
 * @project telegram-bot
 */
public class MessageHandler extends TelegramLongPollingBot {

    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    public MessageHandler() {
        super("6760481369:AAEqeLf-KjCH1efjQcLH1QWlQRC4dtMMiZ0");
    }

    /**
     * onUpdateReceived: This method will be called when an Update is received by your bot. In this example, this method will just read messages and echo the same text:
     * @param update
     */
    @Override
    public void onUpdateReceived(Update update) {
        logger.info("trigger onUpdateReceived ", update);
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                logger.error("Got exception when received msg due to ", e.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "awesome_jobs_bot";
    }

    @Override
    public void onRegister() {
        logger.info("trigger onRegister");
    }
}
