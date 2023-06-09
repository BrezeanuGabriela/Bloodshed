package com.paw.View;

import com.paw.Model.UserManagementService.Entities.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
public class NewUserDTO {
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

    @Getter
    @Setter
    @OneToMany
    private List<RoleInputDTO> roles;

    public NewUserDTO() {}
}
