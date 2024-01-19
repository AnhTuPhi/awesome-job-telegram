package vn.com.tech.telegram.bot;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import vn.com.tech.telegram.bot.handlers.AwesomeJobHandler;

/**
 * @author Admin
 * @created 19/01/2024 - 9:51 AM
 * @project telegram-bot
 */
public class TelegramBotApplication {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotApplication.class);

    public static void main(String[] args) {
        initializeBot();
    }

    public static void initializeBot() {
        try {
            TelegramBotsApi telegramBotApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotApi.registerBot(new AwesomeJobHandler());
        } catch (TelegramApiException e){
            logger.error("Got exception when initialize bot due to ", e.getMessage());
        }
    }
}
