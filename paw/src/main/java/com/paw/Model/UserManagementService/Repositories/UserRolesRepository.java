package com.paw.Model.UserManagementService.Repositories;

import com.paw.Model.UserManagementService.Entities.Role;
import com.paw.Model.UserManagementService.Entities.UserRole;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRolesRepository extends CrudRepository<UserRole, Integer> {
}
