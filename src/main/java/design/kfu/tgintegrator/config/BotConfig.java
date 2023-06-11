package design.kfu.tgintegrator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Bots configurations
 * @date 20 of June of 2015
 */
@Component
public class BotConfig {
    @Value("${bot.token}")
    public static String BOT_TOKEN = "<token>";

    @Value("${bot.name}")
    public static String BOT_USERNAME = "certinformationbot";
}
