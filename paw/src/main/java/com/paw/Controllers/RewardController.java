package com.paw.Controllers;

import com.paw.Exceptions.JWTExceptions.CorruptedJwt;
import com.paw.Exceptions.NotAcceptableReq;
import com.paw.Exceptions.UnprocessableReq;
import com.paw.Exceptions.UserNotFound;
import com.paw.JWT.JwtUtil;
import com.paw.Model.RewardsService.Entities.Reward;
import com.paw.Model.RewardsService.Repositories.RewardRepository;
import com.paw.Model.RewardsService.Services.RewardService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RewardController {
    @Autowired
    RewardService rewardService;
    @Autowired
    public JwtUtil jwtUtil;

    @PutMapping("/api/rewards/users/{idUser}")
    public ResponseEntity<?> claimReward(@RequestHeader Map<String, String> token,
                                        @PathVariable Integer idUser,
                                        @RequestBody Integer rewardId)
    {
        //verificare token
        String tokenLogin = token.get("authorization");

        // token-ul lipseste
        if (tokenLogin == null) {
            return new ResponseEntity<>("Header-ul de autorizare lipseste!", HttpStatus.UNAUTHORIZED);
        }

        Claims claims;
        try {
            tokenLogin = tokenLogin.split(" ")[1];
            // daca se executa cu succes -> a trecut testul de integritate
            claims = jwtUtil.getAllClaimsFromToken(tokenLogin);

            // testul pentru blacklist
            if(jwtUtil.isTokenBlacklisted(claims)) {
                return new ResponseEntity<>("Header-ul de autorizare este invalid!", HttpStatus.UNAUTHORIZED);
            }

            // testul pentru valabilitate
            if (jwtUtil.isJwtExpired(claims)) {
                jwtUtil.addTokenToBlackList(tokenLogin);
                return new ResponseEntity<>("Header-ul de autorizare -token login- este expirat!", HttpStatus.UNAUTHORIZED);
            }

            // testul pentru issuer
            if (!jwtUtil.isIssuerKnown(claims)) {
                jwtUtil.addTokenToBlackList(tokenLogin);
                return new ResponseEntity<>("Issuer not known!", HttpStatus.UNAUTHORIZED);
            }
        }
        // token-ul din header este corupt
        catch (CorruptedJwt corruptedJwt) {
            jwtUtil.addTokenToBlackList(tokenLogin);
            return new ResponseEntity<>("Header-ul de autorizare este corupt!", HttpStatus.UNAUTHORIZED);
        }

        Integer sub = Integer.parseInt(claims.get("sub").toString());
        if(Integer.parseInt(sub.toString()) != idUser) {
            jwtUtil.addTokenToBlackList(tokenLogin);
            return new ResponseEntity<>("Forbidden!", HttpStatus.FORBIDDEN);
        }
        try{
            Reward reward = rewardService.getRewardById(rewardId);
            Boolean result = rewardService.claimReward(idUser, reward);
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
        catch (UserNotFound userNotFound)
        {
            return new ResponseEntity<>(userNotFound.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (DataAccessException dataAccessException)
        {
            return new ResponseEntity<>(dataAccessException.getLocalizedMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        catch (UnprocessableReq | NotAcceptableReq unprocessableReq)
        {
            return new ResponseEntity<>(unprocessableReq.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        catch (RuntimeException runtimeException)
        {
            return new ResponseEntity<>(runtimeException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}