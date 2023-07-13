package com.example.users.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Name {
    private String firstName;
    private String lastName;
}