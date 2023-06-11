package design.kfu.tgintegrator.controller;

import design.kfu.tgintegrator.domain.dto.MessageDTO;
import design.kfu.tgintegrator.domain.model.Message;
import design.kfu.tgintegrator.service.MessageService;
import design.kfu.tgintegrator.telegram.CertWebhookBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.Valid;

@Slf4j
@RestController
//@RequestMapping(value = "/v1")
public class CertMessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/integrate/message")
    public Message sendMessage(@Valid @RequestBody MessageDTO messageDTO) {
        return messageService.sendMessageDTO(messageDTO);
    }
}