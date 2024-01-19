package vn.com.tech.telegram.bot.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import vn.com.tech.telegram.bot.util.JsonMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Admin
 * @created 19/01/2024 - 9:58 AM
 * @project telegram-bot
 */
public class AwesomeJobHandler extends TelegramLongPollingBot {

    private final static Logger logger = LoggerFactory.getLogger(AwesomeJobHandler.class);

    /**
     * Define list of following ids
     */
    public static final Set<Long> followingChatIds = new HashSet<>();

    public static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public AwesomeJobHandler() {
        super("6760481369:AAEqeLf-KjCH1efjQcLH1QWlQRC4dtMMiZ0");
        scheduledExecutorService.scheduleAtFixedRate(this::sendLatestJob, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * onUpdateReceived: This method will be called when an Update is received by your bot. In this example, this method will just read messages and echo the same text:
     *
     * @param update {@link org.telegram.telegrambots.meta.generics.LongPollingBot#onUpdateReceived(Update)}
     */
    @Override
    public void onUpdateReceived(Update update) {
        logger.info("hmmmmm {}", JsonMapper.write(update));
        handleIncomingMessage(update);
    }

    @Override
    public String getBotUsername() {
        return "awesome_jobs_bot";
    }

    @Override
    public void onRegister() {
        logger.info("trigger onRegister");
    }

    /**
     * Handle incoming message and route to valid method
     *
     * @param update
     */
    private void handleIncomingMessage(Update update) {
        handleExecuteDirectMessage(update);
        handleExecuteChannelMessage(update);
        handleInitFollowingChatIDs(update);
        handleUnFollowGroupOrChannel(update);
    }

    private void handleExecuteDirectMessage(Update update) {
        if (Boolean.FALSE.equals(update.hasMessage())
                || Boolean.FALSE.equals(update.getMessage().hasText())
                || isNull(update.getMessage().getChat())
                || Boolean.FALSE.equals(update.getMessage().getChat().isUserChat())) {
            return;
        }
        logger.info("handleExecuteDirectMessage onEvent {}", JsonMapper.write(update));

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(update.getMessage().getText());

        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            logger.error("Got exception when sending msg due to {}", e.getMessage());
        }
    }

    private void handleExecuteChannelMessage(Update update) {
        if (Boolean.FALSE.equals(update.hasChannelPost())
                || Boolean.FALSE.equals(update.getChannelPost().hasText())
                || isNull(update.getMessage())
                || isNull(update.getMessage().getChat())
                || Boolean.FALSE.equals(update.getMessage().getChat().isChannelChat())) {
            return;
        }
        logger.info("handleExecuteChannelMessage onEvent {}", JsonMapper.write(update));

        //prepare data
        var channelPost = update.getChannelPost();
        var channelId = channelPost.getChatId();
        var response = channelPost.getText();

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(channelId));
        message.setText(response);

        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            logger.error("Got exception when sending msg due to {}", e.getMessage());
        }
    }

    private void handleInitFollowingChatIDs(Update update) {
        if (isNull(update.getMessage())
                || isNull(update.getMessage().getNewChatMembers())) {
            return;
        }

        var newMembers = update.getMessage().getNewChatMembers();
        var optNewBot = newMembers.stream().filter(member -> Boolean.TRUE.equals(member.getIsBot())).findAny();
        if (optNewBot.isEmpty()) {
            return;
        }

        var chatId = update.getMessage().getChat().getId();
        followingChatIds.add(chatId);
        logger.info("followingChatIds add chat id: {}", chatId);
    }

    private void sendLatestJob() {
        followingChatIds.forEach(groupChatId -> {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(groupChatId));
            message.setText("test");

            try {
                execute(message); // Call method to send the message
                logger.info("send msg to group already");
            } catch (TelegramApiException e) {
                logger.error("Got exception when sending msg due to {}", e.getMessage());
            }
        });
    }

    private void handleUnFollowGroupOrChannel(Update update) {
        if (isNull(update.getMessage())
                || isNull(update.getMessage().getLeftChatMember())
                || Boolean.FALSE.equals(update.getMessage().getLeftChatMember().getIsBot())) {
            return;
        }
        var chatId = update.getMessage().getChat().getId();
        followingChatIds.remove(chatId);
        logger.info("followingChatIds remove chat id: {}", chatId);
    }
}
