package design.kfu.tgintegrator.controller;

import design.kfu.tgintegrator.domain.dto.MessageDTO;
import design.kfu.tgintegrator.domain.model.Message;
import design.kfu.tgintegrator.service.MessageService;
import design.kfu.tgintegrator.telegram.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class TelegramInfoController {

    @Autowired
    private TelegramService telegramService;

    @GetMapping("/chat/{id}/users/count")
    public Integer getUserCount(@PathVariable("id") String id) {
        return telegramService.getUsersFromChat(id);
    }
}