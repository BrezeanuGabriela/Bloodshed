package com.paw.View.Outputs;

import com.paw.Model.UserManagementService.Entities.InfoUser;
import com.paw.Model.UserManagementService.Entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public class UserOutput {
    @Setter
    @Getter
    private Integer id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private InfoUser infoUser;

    @Getter
    @Setter
    private List<Role> roles;
}
