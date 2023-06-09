package com.paw.Model.UserManagementService.Repositories;

import com.paw.Model.UserManagementService.Entities.Role;
import com.paw.Model.UserManagementService.Entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer>{
    List<User> findAll();
    User findUserById(Integer id);
    User findUserByUsername(String username);
    User findUserByUsernameAndPassword(String username, String password);
}
