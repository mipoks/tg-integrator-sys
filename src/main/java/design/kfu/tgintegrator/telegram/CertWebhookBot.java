package design.kfu.tgintegrator.telegram;

import design.kfu.tgintegrator.domain.dto.CommentDTO;
import design.kfu.tgintegrator.domain.model.Message;
import design.kfu.tgintegrator.exception.ErrorType;
import design.kfu.tgintegrator.exception.Exc;
import design.kfu.tgintegrator.feign.FeignMessageClient;
import design.kfu.tgintegrator.feign.HeaderService;
import design.kfu.tgintegrator.service.MessageService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.abilitybots.api.bot.AbilityWebhookBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.URL;
import java.util.*;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.telegrambots.meta.api.objects.File.getFileUrl;

@Slf4j
@Setter
public class CertWebhookBot extends AbilityWebhookBot implements TelegramBot {

    @Lazy
    @Autowired
    private FeignMessageClient feignMessageClient;

    @Lazy
    @Autowired
    private MessageService messageService;

    @Lazy
    @Autowired
    private HeaderService headerService;

    @Value("${bot.token}")
    private String TOKEN;

    @Value("${bot.name}")
    private String BOT_USERNAME;

    @Value("${bot.path}")
    private String BOT_PATH;

    @Value("${bot.creator}")
    private Long CREATOR_ID;

    @Getter
    private ExecuteApiMethod<Chat> chat = method -> {

        try {
            return sendApiMethod(method);
        } catch (TelegramApiException e) {
            throw Exc.gen(ErrorType.UNEXPECTED_ERROR, e.getMessage());
        }
    };
    @Getter
    private ExecuteApiMethod<Integer> countOfUsers = method -> {
        try {
            return sendApiMethod(method);
        } catch (TelegramApiException e) {
            throw Exc.gen(ErrorType.UNEXPECTED_ERROR, e.getMessage());
        }
    };

    public CertWebhookBot(String token, String username, String path) {
        super(token, username, path);
    }

    @Override
    public boolean checkGlobalFlags(Update update) {
        log.info("result {}", Objects.nonNull(update.getMessage()) && (Objects.nonNull(update.getMessage().getLeftChatMember())
                || Objects.nonNull(update.getMessage().getNewChatMembers())
                || !update.getMessage().getNewChatMembers().isEmpty()));


        return Objects.nonNull(update.getMessage()) && (Objects.nonNull(update.getMessage().getLeftChatMember())
                || Objects.nonNull(update.getMessage().getNewChatMembers())
                || !update.getMessage().getNewChatMembers().isEmpty());
    }


    @Override
    public String getBotUsername() {
        log.info("BOT_USERNAME {}", BOT_USERNAME);
        return BOT_USERNAME;
    }

    @Override
    public long creatorId() {
        return CREATOR_ID;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }


    public Message sendMessage(Message message) {
        try {
            Integer messageId = execute(
                    SendMessage.builder()
                            .chatId(message.getTelegramChatId())
                            .text(message.buildFormattedText())
                            .build()
            ).getMessageId();
            message.setTelegramMessageId(Long.valueOf(messageId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return message;
    }

    private boolean isBotAnswered(org.telegram.telegrambots.meta.api.objects.Message message) {
        Optional<Message> msg = messageService.findMessageByTelegramMessageId(Long.valueOf(message.getMessageId()));
        if (msg.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    private Message getBotAnswered(org.telegram.telegrambots.meta.api.objects.Message message) {
        return messageService.findMessageByTelegramMessageId(Long.valueOf(message.getMessageId())).get();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            log.info("update {}", update);
            if (!update.hasMessage()) {
                return null;
            }
            org.telegram.telegrambots.meta.api.objects.Message msg = update.getMessage();
            org.telegram.telegrambots.meta.api.objects.Message reply = update.getMessage().getReplyToMessage();

            List<PhotoSize> photos = update.getMessage().getPhoto();
            if (photos == null) photos = new ArrayList<>();
            if (photos.size() > 0) {
                if (msg.getCaption() == null || msg.getCaption().equals("")) {
                    msg.setText("Прикрепленное изображение");
                } else {
                    msg.setText(msg.getCaption());
                }
            }


            if (reply != null) {
                if (isBotAnswered(reply) && msg.hasText()) {
                    Message m = getBotAnswered(reply);
                    Long forumMessageId = m.getMessageId();
                    CommentDTO commentDTO = CommentDTO.builder()
                            .clubId(m.getClubId())
                            .answered(m.getMessageId())
                            .value(msg.getText())
                            .telegramAccountId(update.getMessage().getFrom().getId())
                            .build();
                    log.info("message should be sent to forum {}", commentDTO);
                    String auth = headerService.getAuthorizationBase64ForId(commentDTO.getTelegramAccountId());
                    CommentDTO createdComment = feignMessageClient.sendComment(auth, m.getClubId(), commentDTO);
                    if (createdComment != null) {
                        Long createdCommentId = createdComment.getId();

                        Optional<PhotoSize> maxPhoto = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize));
                        if (maxPhoto.isPresent()) {
                            PhotoSize p = maxPhoto.get();
                            GetFile getFile = GetFile.builder().fileId(p.getFileId())
                                    .build();
                            File execute = execute(getFile);
                            String filePath = execute.getFilePath();
                            String fileUrl = getFileUrl(getBotToken(), filePath);

                            URL website = new URL(fileUrl);
                            try (InputStream in = website.openStream()) {
                                MultipartFile multipartFile = new MockMultipartFile("image", in);
                                feignMessageClient.uploadFile(createdCommentId.toString(), multipartFile);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String getBotPath() {
        log.info(" bot paath {}", BOT_PATH);
        return BOT_PATH;
    }


}
