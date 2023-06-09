package com.paw.Model.RewardsService.Services;

import com.paw.Exceptions.RewardNotFound;
import com.paw.Exceptions.UnprocessableReq;
import com.paw.Exceptions.UserNotFound;
import com.paw.Model.LogService.Services.LogService;
import com.paw.Model.RewardsService.Entities.Reward;
import com.paw.Model.RewardsService.Repositories.RewardRepository;
import com.paw.Model.UserManagementService.Entities.InfoUser;
import com.paw.Model.UserManagementService.Entities.User;
import com.paw.Model.UserManagementService.Repositories.UserInfoRepository;
import com.paw.Model.UserManagementService.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service

public class RewardService {
    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    LogService logService;
    public Reward getRewardById(Integer id)
    {
        Reward reward = rewardRepository.findRewardById(id);

        if(reward != null)
            return reward;
        else
            throw new RewardNotFound("Reward-ul cu id-ul " + id.toString() + " nu a fost gasit!");
    }

    public Boolean claimReward(Integer userId, Reward reward) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFound("User not found!");
        }
        try
        {
            Integer currentPoints = user.getInfoUser().getPuncte();
            if(currentPoints >= reward.getPrice())
            {
                InfoUser newUserInfo = user.getInfoUser();
                newUserInfo.setPuncte(currentPoints - reward.getPrice());

                userInfoRepository.save(newUserInfo);
                user.setInfoUser(newUserInfo);
                userRepository.save(user);

                logService.addLog("User " + user.getUsername()
                        + " redeemed " + reward.getName() + " with " + reward.getPrice() + " points");
                return true;
            }
            else
            {
                logService.addLog("User " + user.getUsername() + " unsuccessfully requested a redeem");
                return false;
            }
        }
        catch (DataAccessException dataAccessException)
        {
            throw dataAccessException;
        }
        catch (IllegalArgumentException illegalArgumentException)
        {
            throw new UnprocessableReq("Unprocessable request body!");
        }
        catch (NullPointerException nullPointerException)
        {
            throw new UnprocessableReq("Details about user not found!");
        }
        catch (RuntimeException runtimeException)
        {
            System.out.println(runtimeException.getMessage());
            throw runtimeException;
        }
    }
}
