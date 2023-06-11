package design.kfu.tgintegrator.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * This commands starts the conversation with the bot
 *
 * @author Timo Schulz (Mit0x2)
 */
@Slf4j
public class GetChatInfoCommand extends BotCommand {

    public static final String LOGTAG = "STARTCOMMAND";

    public GetChatInfoCommand() {
        super("chat", "This command return Chat ID of group");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(String.valueOf(chat.getId()));

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error(LOGTAG, e);
        }
    }
}