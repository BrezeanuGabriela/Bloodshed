package com.paw.Model.RewardsService.Repositories;

import com.paw.Model.LogService.Entities.Log;
import com.paw.Model.RewardsService.Entities.Reward;
import com.paw.Model.UserManagementService.Entities.User;
import org.springframework.data.repository.CrudRepository;

public interface RewardRepository extends CrudRepository<Reward, Integer>{
    Reward findRewardById(Integer id);
}
