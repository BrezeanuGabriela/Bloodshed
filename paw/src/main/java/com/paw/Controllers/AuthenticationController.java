package com.paw.Controllers;

import com.paw.Exceptions.JWTExceptions.*;
import com.paw.Exceptions.UserNotFound;
import com.paw.JWT.JwtUtil;
import com.paw.Model.AuthenticationService.Services.AuthenticationService;
import com.paw.View.LoginDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    public JwtUtil jwtUtil;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class TokenClass {
        private String loginToken;
    }

    @PostMapping("/api/authentication/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO)
    {
        try {
            String loginToken = authenticationService.login(loginDTO);
            return new ResponseEntity<>(new TokenClass(loginToken), HttpStatus.CREATED);
        }
        catch (UserNotFound userNotFound)
        {
            return new ResponseEntity<>(userNotFound.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/authentication/authorize")
    public ResponseEntity<?> authorize(@RequestBody String loginToken)
    {
        try {
            String authorizeToken = authenticationService.authorize(loginToken);
            return new ResponseEntity<>(authorizeToken, HttpStatus.CREATED);
        }
        catch (UserNotFound userNotFound)
        {
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        }
        catch (JwtNotFound jwtNotFound)
        {
            return new ResponseEntity<>("LoginToken not found", HttpStatus.UNAUTHORIZED);
        }
        catch (CorruptedJwt corruptedJwt)
        {
            return new ResponseEntity<>(corruptedJwt.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch (IssuerNotKnown issuerNotKnown)
        {
            return new ResponseEntity<>("Issuer not known!", HttpStatus.UNAUTHORIZED);
        }
        catch (ExpiredJwt expiredJwt)
        {
            return new ResponseEntity<>("LoginToken is expired!", HttpStatus.UNAUTHORIZED);
        }
        catch (InvalidJwt invalidJwtException)
        {
            return new ResponseEntity<>("LoginToken invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/authentication/logout")
    public ResponseEntity<?> logout(@RequestBody String loginToken)
    {
        try {
            authenticationService.logout(loginToken);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (JwtNotFound jwtNotFound)
        {
            return new ResponseEntity<>(jwtNotFound.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
