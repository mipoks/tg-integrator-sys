package design.kfu.tgintegrator.service;

import design.kfu.tgintegrator.domain.dto.MessageDTO;
import design.kfu.tgintegrator.domain.model.Message;
import design.kfu.tgintegrator.repository.MessageRepository;
import design.kfu.tgintegrator.telegram.CertWebhookBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class MessageService {

    @Autowired
    private CertWebhookBot certWebhookBot;

    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessageDTO(MessageDTO messageDTO) {
        Message message = certWebhookBot.sendMessage(MessageDTO.toMessage(messageDTO));
        return messageRepository.save(message);
    }

    public Optional<Message> findMessageByTelegramMessageId(Long telegramMessageId) {
        return messageRepository.findByTelegramMessageId(telegramMessageId);
    }

}
