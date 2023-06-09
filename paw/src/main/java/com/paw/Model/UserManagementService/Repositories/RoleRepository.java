package com.paw.Model.UserManagementService.Repositories;

import com.paw.Model.UserManagementService.Entities.InfoUser;
import com.paw.Model.UserManagementService.Entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer>{
    Role findRoleByRoleName(String roleName);
}
