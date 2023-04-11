package ru.didenko.smartconsulting.seasonalservices.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.didenko.smartconsulting.seasonalservices.dto.GenericDto;
import ru.didenko.smartconsulting.seasonalservices.exceptions.IncorrectIdException;
import ru.didenko.smartconsulting.seasonalservices.mapper.GenericMapper;
import ru.didenko.smartconsulting.seasonalservices.model.GenericModel;
import ru.didenko.smartconsulting.seasonalservices.service.GenericService;

import java.util.List;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
public abstract class GenericController<T extends GenericModel, N extends GenericDto> {

    private final GenericService<T> service;
    private final GenericMapper<T, N> mapper;

    public GenericController(GenericService<T> service, GenericMapper<T, N> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @ResponseBody
    @GetMapping("/list")
    @Operation(description = "Получить список всех записей", method = "GetAll")
    public ResponseEntity<List<N>> getAll() {
        return ResponseEntity.ok().body(mapper.toDtos(service.getList()));
    }

    @ResponseBody
    @GetMapping("/get-one/{id}")
    @Operation(description = "Получить запись по id", method = "GetById")
    public ResponseEntity<?> getById(@PathVariable Long id) throws IncorrectIdException {
        try {
            return ResponseEntity.ok().body(mapper.toDto(service.getOneById(id)));
        } catch (Exception e) {
            throw new IncorrectIdException("Element with id " + id + " doesn't exist!");
        }
    }

    @ResponseBody
    @PostMapping("/create")
    @Operation(description = "Создать новую запись", method = "Create")
    public ResponseEntity<N> create(@RequestBody N object) {
        return ResponseEntity.ok().body(mapper.toDto(service.create(mapper.toEntity(object))));
    }

    @ResponseBody
    @PutMapping("/update/{id}")
    @Operation(description = "Обновить запись по id", method = "Update")
    public ResponseEntity<N> update(@PathVariable Long id, @RequestBody N object) {
        object.setId(id);
        return ResponseEntity.ok().body(mapper.toDto(service.update(mapper.toEntity(object))));
    }

    @ResponseBody
    @DeleteMapping("/delete/{id}")
    @Operation(description = "Удалить запись по id", method = "Delete")
    public void delete(@PathVariable Long id) throws IncorrectIdException {
        try {
            service.delete(id);
        } catch (Exception e) {
            throw new IncorrectIdException("Element with id " + id + " doesn't exist!");
        }
    }
}