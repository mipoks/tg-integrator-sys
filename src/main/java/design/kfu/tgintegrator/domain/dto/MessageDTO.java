package design.kfu.tgintegrator.domain.dto;

import design.kfu.tgintegrator.domain.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {

    @NotNull
    private Long telegramChatId;
    private String userName;
    private Long messageId;
    private Long clubId;
    @NotNull
    private String text;
    private List<String> urls = new ArrayList<>();

    public static Message toMessage(MessageDTO messageDTO) {
        return Message.builder()
                .telegramChatId(messageDTO.getTelegramChatId())
                .messageId(messageDTO.getMessageId())
                .userName(messageDTO.getUserName())
                .clubId(messageDTO.getClubId())
                .text(messageDTO.getText())
                .urls(messageDTO.getUrls())
                .build();
    }
}
