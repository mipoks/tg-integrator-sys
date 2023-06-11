package design.kfu.tgintegrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {

    private Long id;
    private String value;
    private Long accountId;
    private Long clubId;
    private Long answered;
    private Long telegramAccountId;
}
