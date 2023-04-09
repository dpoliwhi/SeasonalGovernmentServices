package ru.didenko.smartconsulting.seasonalservices.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Table(
        name = "users",
        uniqueConstraints = {
//                @UniqueConstraint(name = "uk_unique_email", columnNames = "email")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "users_seq", allocationSize = 1)
public class User extends GenericModel {

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "midlname")
    private String midlName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(name = "FK_USER_ROLE")
    )
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Application> applications;
}

