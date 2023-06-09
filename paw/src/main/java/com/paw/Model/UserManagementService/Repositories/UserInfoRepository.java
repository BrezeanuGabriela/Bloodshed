package com.paw.Model.UserManagementService.Repositories;

import com.paw.Model.UserManagementService.Entities.InfoUser;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<InfoUser, Integer> {
}
