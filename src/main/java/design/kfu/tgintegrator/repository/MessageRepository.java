package design.kfu.tgintegrator.repository;

import design.kfu.tgintegrator.domain.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
public interface MessageRepository extends CrudRepository<Message, Long> {
    Optional<Message> findByTelegramMessageId(Long messageId);
}
