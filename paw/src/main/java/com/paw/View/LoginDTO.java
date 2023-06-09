package com.paw.View;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LoginDTO {
    @Id
    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    private String username;

    @Setter
    @Getter
    private String password;
}
