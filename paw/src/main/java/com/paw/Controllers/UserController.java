package com.paw.Controllers;

import com.paw.Exceptions.JWTExceptions.CorruptedJwt;
import com.paw.Exceptions.JWTExceptions.InvalidJwt;
import com.paw.Exceptions.NotAcceptableReq;
import com.paw.Exceptions.UnprocessableReq;
import com.paw.Exceptions.UserAlreadyExists;
import com.paw.Exceptions.UserNotFound;
import com.paw.JWT.JwtUtil;
import com.paw.View.NewUserDTO;
import com.paw.Model.UserManagementService.Entities.User;
import com.paw.Model.UserManagementService.Services.UserService;
import com.paw.View.Outputs.UserOutput;
import com.paw.View.UpdateUserDTO;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    public JwtUtil jwtUtil;

    public UserOutput convertUserToDTO(User user)
    {
        return new UserOutput(user.getId(), user.getUsername(), user.getPassword(), user.getInfoUser(), user.getRoles());
    }

    @GetMapping("/api/users/")
    public ResponseEntity<?> getAllUsers()
    {
        List<User> users = userService.getUsers();
//        List<UserOutput> outputs = users.stream().map(user -> convertUserToDTO(user)).collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/api/users/{info}")
    public ResponseEntity<?> getUserBy( @RequestHeader Map<String, String> token,
                                        @PathVariable String info,
                                        @RequestParam String field)
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

        User user = null;
        try {
            if (field.equals("id")) {
                try {
                    user = userService.getUserById(Integer.parseInt(info));
                } catch (NumberFormatException numberFormatException) {
                    return new ResponseEntity<>("Id must be a number, not a string!", HttpStatus.NOT_ACCEPTABLE);
                }
            } else if (field.equals("username")) {
                user = userService.getUserByUsername(info);
            }
            else {
                return new ResponseEntity<>("Path not found! Field must be idUser/username!", HttpStatus.NOT_FOUND);
            }
        }
        catch (UserNotFound userNotFound)
        {
            return new ResponseEntity<>(userNotFound.getMessage(), HttpStatus.NOT_FOUND);
        }

        Integer sub = Integer.parseInt(claims.get("sub").toString());
        if(Integer.parseInt(sub.toString()) == user.getId()) {
            return new ResponseEntity<>(convertUserToDTO(user), HttpStatus.OK);
        }
        else{
            jwtUtil.addTokenToBlackList(tokenLogin);
            return new ResponseEntity<>("Forbidden!", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/api/users/")
    public ResponseEntity<?> addUser(@RequestBody NewUserDTO newUserDTO)
    {
        try {
            User newUser = userService.addUser(newUserDTO);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
        catch (UserAlreadyExists userAlreadyExists)
        {
            return new ResponseEntity<>(userAlreadyExists.getMessage(), HttpStatus.CONFLICT);
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

    @PutMapping("/api/users/{idUser}")
    public ResponseEntity<?> updateUser(@RequestHeader Map<String, String> token,
                                        @PathVariable Integer idUser,
                                        @RequestBody UpdateUserDTO updateUserDTO)
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
            userService.updateUser(updateUserDTO, idUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    @DeleteMapping("/api/users/{userInfo}")
    public ResponseEntity<?> deleteUserBy(@RequestHeader Map<String, String> token,
                                            @PathVariable String userInfo,
                                            @RequestParam String field)
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

        User user = null;
        try{
            if (field.equals("idUser")) {
                user = userService.deleteUserById(Integer.parseInt(userInfo));
            }
            else if (field.equals("username")) {
                user = userService.deleteUserByUsername(userInfo);
            }
            else {
                return new ResponseEntity<>("Path not found! Field must be idUser/username!", HttpStatus.NOT_FOUND);
            }
        }
        catch (UserNotFound userNotFound)
        {
            return new ResponseEntity<>(userNotFound.getMessage(), HttpStatus.NOT_FOUND);
        }

        Integer sub = Integer.parseInt(claims.get("sub").toString());
        if(Integer.parseInt(sub.toString()) == user.getId()) {
            return new ResponseEntity<>(convertUserToDTO(user), HttpStatus.OK);
        }
        else{
            jwtUtil.addTokenToBlackList(tokenLogin);
            return new ResponseEntity<>("Forbidden!", HttpStatus.FORBIDDEN);
        }
    }
}
