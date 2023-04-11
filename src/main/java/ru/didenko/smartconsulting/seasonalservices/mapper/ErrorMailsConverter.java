package ru.didenko.smartconsulting.seasonalservices.mapper;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import ru.didenko.smartconsulting.seasonalservices.dto.ErrorMailsDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class to convert Map of mail messages and their exceptions to Dto
 */
@Component
public class ErrorMailsConverter {

    public List<ErrorMailsDto> convertToDtos(Map<SimpleMailMessage, MailException> mailExceptionMap) {
        return mailExceptionMap
                .entrySet()
                .stream()
                .map(i -> new ErrorMailsDto(
                        Arrays.stream(Objects.requireNonNull(i.getKey().getTo())).toList(),
                        i.getValue().getMessage())
                )
                .toList();
    }
}
