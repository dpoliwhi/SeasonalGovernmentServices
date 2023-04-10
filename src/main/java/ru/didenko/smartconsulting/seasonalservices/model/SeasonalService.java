package ru.didenko.smartconsulting.seasonalservices.model;

import javax.persistence.*;
import javax.validation.constraints.Min;

import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "service_seq", allocationSize = 1)
public class SeasonalService extends GenericModel {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "date_start")
    private LocalDate dateStart;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Column(name = "balance", columnDefinition = "bigint check (balance >= 0)")
    private Long balance;
}
