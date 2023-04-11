package ru.didenko.smartconsulting.seasonalservices.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.didenko.smartconsulting.seasonalservices.dto.LoginDto;
import ru.didenko.smartconsulting.seasonalservices.dto.UserDto;
import ru.didenko.smartconsulting.seasonalservices.dto.UserWithApplicationsDto;
import ru.didenko.smartconsulting.seasonalservices.exceptions.ConstraintsException;
import ru.didenko.smartconsulting.seasonalservices.exceptions.IncorrectIdException;
import ru.didenko.smartconsulting.seasonalservices.mapper.UserMapper;
import ru.didenko.smartconsulting.seasonalservices.mapper.UserWithApplicationsMapper;
import ru.didenko.smartconsulting.seasonalservices.model.User;
import ru.didenko.smartconsulting.seasonalservices.security.JwtTokenUtil;
import ru.didenko.smartconsulting.seasonalservices.service.UserService;
import ru.didenko.smartconsulting.seasonalservices.service.userDetails.CustomUserDetails;
import ru.didenko.smartconsulting.seasonalservices.service.userDetails.CustomUserDetailsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/user")
public class UserController extends GenericController<User, UserDto> {

    private final UserMapper mapper;
    private final UserService service;
    private final UserWithApplicationsMapper userWithApplicationsMapper;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(
            UserMapper mapper,
            UserService service,
            UserWithApplicationsMapper userWithApplicationsMapper,
            CustomUserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil
    ) {
        super(service, mapper);
        this.service = service;
        this.userWithApplicationsMapper = userWithApplicationsMapper;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.mapper = mapper;
    }

    @GetMapping("/user-applications")
    @Operation(description = "Вывести всех клиентов с заявками", method = "GetUserWithApplications")
    public List<UserWithApplicationsDto> getUserWithApplications() {
        return userWithApplicationsMapper.toDtos(service.getList());
    }

    @PostMapping("/auth")
    @Operation(description = "Авторизоваться", method = "Auth")
    public ResponseEntity<?> auth(@RequestBody LoginDto loginDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            CustomUserDetails foundUser = (CustomUserDetails) userDetailsService.loadUserByUsername(loginDto.getLogin());
            if (!service.checkPassword(loginDto)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("WrongPassword");
            }
            String token = jwtTokenUtil.generateToken(foundUser);
            response.put("token", token);
            response.put("id", foundUser.getUserId());
            response.put("login", foundUser.getUsername());
            response.put("authorities", foundUser.getAuthorities());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No such user exist");
        }
    }

    @Override
    @ResponseBody
    @Operation(description = "Создать аккаунт нового клиента", method = "Create")
    public ResponseEntity<UserDto> create(@RequestBody UserDto object) {
        return ResponseEntity.ok().body(mapper.toDto(service.create(mapper.toEntity(object))));
    }

    @Override
    @ResponseBody
    @PutMapping("/update/{id}")
    @Operation(description = "Обновить пользователя по id", method = "Update")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto object) {
        try {
            object.setId(id);
            return ResponseEntity.ok().body(mapper.toDto(service.update(mapper.toEntity(object))));
        } catch (ConstraintsException e) {
            throw e;
        } catch (Exception e) {
            throw new IncorrectIdException("No such user with id " + id + " exists!");
        }
    }

    @Override
    @ResponseBody
    @GetMapping("/get-one/{id}")
    @Operation(description = "Получить данные пользователя по id", method = "GetById")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(mapper.toDto(service.getOneById(id)));
        } catch (Exception e) {
            throw new IncorrectIdException("No such user with id " + id + " exists!");
        }
    }

    @ResponseBody
    @PostMapping("/create-manager")
    @Operation(description = "Создать аккаунт нового менеджера", method = "CreateManager")
    public ResponseEntity<UserDto> createManager(@RequestBody UserDto object) {
        return ResponseEntity.ok().body(mapper.toDto(service.createManager(mapper.toEntity(object))));
    }
}
