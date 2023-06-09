package com.paw.Model.AuthenticationService.Services;

import com.paw.Exceptions.JWTExceptions.*;
import com.paw.Exceptions.UserNotFound;
import com.paw.JWT.JwtUtil;
import com.paw.Model.UserManagementService.Entities.User;
import com.paw.Model.UserManagementService.Services.UserService;
import com.paw.View.LoginDTO;
import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Base64;

@NoArgsConstructor
@Service
public class AuthenticationService {
    @Autowired
    UserService userService;

    @Autowired
    public JwtUtil jwtUtil;

    public String login(LoginDTO loginDTO)
    {
        try{
            User user = userService.loginUser(loginDTO.getUsername(), loginDTO.getPassword());
            String loginToken = jwtUtil.createLoginToken(user);
            return loginToken;
        }
        catch (UserNotFound userNotFound)
        {
            throw userNotFound;
        }
    }

    public String authorize(String loginToken)
    {
        if(loginToken == null || loginToken.equals("") || loginToken.split("\\.").length < 3)
        {
            throw new JwtNotFound();
        }

        try {
            // daca se executa cu succes -> a trecut testul de integritate
            Claims allClaims = jwtUtil.getAllClaimsFromToken(loginToken);

            JSONObject jsonObject = jwtUtil.readBlackList();
            String jti = (String) allClaims.get("jti");
            if(jsonObject.containsKey(jti))
            {
                throw new InvalidJwt();
            }

            if(jwtUtil.isJwtExpired(allClaims))
            {
                throw new ExpiredJwt();
            }

            if(!jwtUtil.isIssuerKnown(allClaims))
            {
                throw new IssuerNotKnown();
            }

            Integer idUser = Integer.parseInt(allClaims.get("sub").toString());
            User user = userService.getUserById(idUser);
            if(user == null)
                throw new UserNotFound();

            String authorizeToken = jwtUtil.createAuthorizeToken(user);

            return authorizeToken;
        }
        catch (CorruptedJwt corruptedJwt)
        {
            throw corruptedJwt;
        }
    }

    public void logout(String loginToken) {
        if (loginToken == null || loginToken.equals("") || loginToken.split("\\.").length < 3) {
            throw new JwtNotFound();
        }

        JSONObject obj = jwtUtil.readBlackList();

        try{
            // daca se executa cu succes -> a trecut testul de integritate
            Claims allClaims = jwtUtil.getAllClaimsFromToken(loginToken);
            String jti = (String) allClaims.get("jti");
            if(jti != null)
            {
                obj.put(jti, loginToken);
                jwtUtil.writeBlackList(obj);
            }
        }
        catch (CorruptedJwt corruptedJwt)
        {
            // decode manual
            String[] chunks = loginToken.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            String payload = new String(decoder.decode(chunks[1]));
            try {
                JSONObject payloadJson = (JSONObject) new JSONParser().parse(payload);
                String jti = (String) payloadJson.get("jti");
                if (jti != null) {
                    obj.put(jti, loginToken);
                    jwtUtil.writeBlackList(obj);
                }
            }
            catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        }
    }
}
