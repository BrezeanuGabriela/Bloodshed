package com.paw.View;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class UpdateUserDTO {
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

    @Getter
    @Setter
    @OneToOne
    private NewUserInfoDTO infoUserDTO;
}
