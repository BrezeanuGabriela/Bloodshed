package com.paw.Model.UserManagementService.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    @Column(name="role_name")
    String roleName;

//    @Setter
//    @Getter
//    @ManyToMany
//    @JoinTable(name = "user_roles",
//            joinColumns = @JoinColumn(name = "id_user"),
//            inverseJoinColumns = @JoinColumn(name = "id_role"))
//    public List<User> users = new ArrayList<>();

    public Role(String roleName)
    {
        this.roleName = roleName;
    }
}
