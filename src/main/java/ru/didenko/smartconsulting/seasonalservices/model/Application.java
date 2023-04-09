package ru.didenko.smartconsulting.seasonalservices.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "application")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "application_seq", allocationSize = 1)
public class Application extends GenericModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "FK_APPLICATION_USER")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "service_id",
            foreignKey = @ForeignKey(name = "FK_SERVICE_USER")
    )
    private SeasonalService service;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed = false;
}
