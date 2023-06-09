package com.paw.Model.UserManagementService.Entities;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name="user_roles")
public class UserRole {
    @Id
    private Integer id_user;

    private Integer id_role;

    public UserRole() {

    }
}
