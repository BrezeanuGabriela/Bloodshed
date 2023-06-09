package com.paw.View;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="newRoleForUser")
public class RoleInputDTO {
    @Id
    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    private String role_name;
}
