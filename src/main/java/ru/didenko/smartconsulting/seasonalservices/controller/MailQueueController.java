package ru.didenko.smartconsulting.seasonalservices.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.didenko.smartconsulting.seasonalservices.dto.ErrorMailsDto;
import ru.didenko.smartconsulting.seasonalservices.mapper.ErrorMailsConverter;
import ru.didenko.smartconsulting.seasonalservices.service.MailQueue;

import java.util.List;

@RestController
@RequestMapping("/rest/mail-query")
public class MailQueueController {

    private final MailQueue mailQueue;
    private final ErrorMailsConverter mailsConverter;

    public MailQueueController(MailQueue mailQueue, ErrorMailsConverter mailsConverter) {
        this.mailQueue = mailQueue;
        this.mailsConverter = mailsConverter;
    }

    @GetMapping("/isRunning")
    @Operation(
            description = "Получить статус работы сервиса-очереди отпревки email сообщений юзерам.",
            method = "IsRunning")
    public ResponseEntity<Boolean> isRunning() {
        return ResponseEntity.ok().body(mailQueue.isRunning());
    }

    @GetMapping("/error-mails-list")
    @Operation(
            description = "Получить список неотправленных сообщений с адресами и ошибками отправки",
            method = "GetErrorMails")
    public ResponseEntity<List<ErrorMailsDto>> getErrorMails() {
        return ResponseEntity.ok().body(mailsConverter.convertToDtos(mailQueue.getMailsWithErrors()));
    }
}
