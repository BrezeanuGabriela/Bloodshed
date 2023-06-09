package com.paw.Model.UserManagementService.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.paw.View.NewUserInfoDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "info_user")
public class InfoUser {
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


    @Getter
    @Column(name="data_nasterii")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    @Setter
    private Date data_nasterii;

    @Column(name="grupa_sange")
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

    @Setter
    @Getter
    private Integer puncte;

    public InfoUser(NewUserInfoDTO newUserInfoDTO)
    {
        this.nume = newUserInfoDTO.getNume();
        this.prenume = newUserInfoDTO.getPrenume();
        this.email = newUserInfoDTO.getNume();
        this.greutate = newUserInfoDTO.getGreutate();
        this.inaltime = newUserInfoDTO.getInaltime();
        this.grupaSange = newUserInfoDTO.getGrupaSange();
        this.puncte = 0;
        this.telefon = newUserInfoDTO.getTelefon();
    }

//    public Date getData_nasterii()
//    {
//        return
//    }
}
