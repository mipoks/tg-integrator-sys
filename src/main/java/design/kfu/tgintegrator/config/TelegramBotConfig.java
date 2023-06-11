package design.kfu.tgintegrator.config;

import design.kfu.tgintegrator.feign.FeignMessageClient;
import design.kfu.tgintegrator.telegram.CertWebhookBot;
import feign.Feign;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;


@Slf4j
@Configuration
public class TelegramBotConfig {

    public static final String TELEGRAM_AUTH_HEADER = "XNET";
    @Value("${bot.token}")
    private String TOKEN;

    @Value("${bot.name}")
    private String BOT_USERNAME;


    @Value("${bot.callback}")
    private String BOT_CALLBACK;

    @Autowired
    private Environment environment;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder()
                .secretToken(TELEGRAM_AUTH_HEADER)
                .url(environment.getProperty("bot.callback"))
                .build();
    } // public address, now it is ngrok, in the future it will (i think) be the server address

    // Create it as
    @Bean
    public CertWebhookBot telegramBot(SetWebhook setWebhookInstance) throws TelegramApiException {

        environment.getProperty("bot.token");
        environment.getProperty("bot.name");

        CertWebhookBot certWebhookBot = new CertWebhookBot(TOKEN, BOT_USERNAME, "");
        certWebhookBot.setBOT_USERNAME(BOT_USERNAME);
        certWebhookBot.setTOKEN(TOKEN);
        certWebhookBot.setBOT_PATH("");
        log.info("bot token is {}", TOKEN);

        DefaultWebhook defaultWebhook = new DefaultWebhook();
        defaultWebhook.setInternalUrl(
                "http://localhost:443"); // the port to start the server, on the localhost computer, on the server it
        // be the server address
        defaultWebhook.registerWebhook(certWebhookBot);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
        log.info("SetWebHook from bot {}", setWebhookInstance);
        telegramBotsApi.registerBot(certWebhookBot, setWebhookInstance);
        return certWebhookBot;

    }

}
