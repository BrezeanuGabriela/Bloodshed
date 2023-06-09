package com.paw.View;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="newInfoUser")
public class NewUserInfoDTO {
    @Id
    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    private String nume;

    @Setter
    @Getter
    private String prenume;

    @Setter
    @Getter
    private String data_nasterii;

    @Setter
    @Getter
    private String grupaSange;

    @Setter
    @Getter
    private String telefon;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private Integer inaltime;

    @Setter
    @Getter
    private Integer greutate;
}
