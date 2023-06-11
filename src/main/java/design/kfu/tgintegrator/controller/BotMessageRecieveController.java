package design.kfu.tgintegrator.controller;

import design.kfu.tgintegrator.telegram.CertWebhookBot;
import design.kfu.tgintegrator.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Controller
public class BotMessageRecieveController {

    public static final String CALLBACK_ENDPOINT = "/callback";
    @Lazy
    @Autowired
    private CertWebhookBot certWebhookBot;

    @PostMapping(CALLBACK_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    public void getUpdateWithDifferentUrl(@RequestBody Update update) {
        log.info("some update recieved from callback/adam path {}", update.toString());
        certWebhookBot.onWebhookUpdateReceived(update);
    }
}