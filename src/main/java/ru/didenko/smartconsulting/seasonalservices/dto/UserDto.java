package ru.didenko.smartconsulting.seasonalservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends GenericDto {

    private String login;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String midlName;

    private RoleDto role;

    private Set<Long> applicationsId;
}
