package design.kfu.tgintegrator.domain.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "integrator_message", timeToLive = 60 * 60 * 24 * 5 * 2 /* 10 суток */)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Indexed
    private Long telegramMessageId;
    private Long telegramChatId;
    private Long clubId;

    private String userName;
    private Long messageId;

    private String text;
    private List<String> urls = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Message account = (Message) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String buildFormattedText() {
        StringBuilder stringBuilder = new StringBuilder("Комментарий пользователя");
        stringBuilder.append(' ').append(userName).append(":\n");
        stringBuilder.append(text);
        return stringBuilder.toString();
    }
}

//Добавить в Redis сущность Message ((Long) id, chatId, (Long) telegramMessageId (генерится после отправки), (forum) userName, text, photoUrl)