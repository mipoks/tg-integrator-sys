package design.kfu.tgintegrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class TgIntegratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TgIntegratorApplication.class, args);
    }

}

//ToDo
//Добавить в Redis сущность Message ((Long) id, chatId, (Long) telegramMessageId (генерится после отправки), (forum) userName, text, photoUrl)
//
//MessageController, который принимает Post запросы dto Message и отсылает их в чат
//
//в CertWebhookBot
//1. Проверить сообщение на команду.
//2. Проверить, что сообщение - это ответ боту.
//3. Проверить наличие в ответе документов и фото.
//4. Отослать все или выполнить команду, если автор имеет права.