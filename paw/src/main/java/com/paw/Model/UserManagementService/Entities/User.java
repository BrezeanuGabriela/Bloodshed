package com.paw.Model.UserManagementService.Entities;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private Integer id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Setter
    @Getter
    @OneToOne(cascade =  CascadeType.PERSIST)
    @JoinColumn(name="id", referencedColumnName = "id")
    private InfoUser infoUser;

    @Setter
    @Getter
    @ManyToMany(cascade =  CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name="user_roles",
            joinColumns = @JoinColumn(name="id_user"),
            inverseJoinColumns = @JoinColumn(name="id_role")
    )
    private List<Role> roles = new ArrayList<>();

    public User() {}

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, InfoUser infoUser, List<Role> roles)
    {
        this.username = username;
        this.password = password;
        this.infoUser = infoUser;
        System.out.println(roles);
//        this.roles = roles;
    }
}
