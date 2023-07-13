package com.example.users.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "endusers")
public class EndUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private Integer organisationId;
    @Embedded
    private Name name;
    private Integer externalId;
    private String password;
    private boolean isActive;
}
