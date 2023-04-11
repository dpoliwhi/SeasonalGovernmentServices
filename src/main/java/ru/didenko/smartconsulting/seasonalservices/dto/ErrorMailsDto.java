package ru.didenko.smartconsulting.seasonalservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMailsDto {

    private List<String> mailTo;

    private String exceptionMessage;
}
