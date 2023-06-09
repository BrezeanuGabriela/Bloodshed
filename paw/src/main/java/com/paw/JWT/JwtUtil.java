package com.paw.JWT;

import com.paw.Exceptions.JWTExceptions.CorruptedJwt;
import com.paw.Exceptions.JWTExceptions.InvalidJwt;
import com.paw.Model.UserManagementService.Entities.Role;
import com.paw.Model.UserManagementService.Entities.User;
import io.jsonwebtoken.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtUtil implements Serializable {
    private Integer NO_MIN = 60;
    private Integer NO_SECONDS = 60;
    private Integer MILIS = 1000;
    private String secret= "secret";
    private String ISSUER = "http://127.0.0.1:8080";
    private String BLACKLIST_PATH = "src/main/resources/blacklist.json";
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public String createLoginToken(User user)
    {
        // sign JWT with ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        // set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuer(ISSUER)
                .setId(uuidAsString)
                .signWith(signatureAlgorithm, signingKey);

        // add the expiration
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + MILIS * NO_MIN * NO_SECONDS;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        return builder.compact();
    }

    public String createAuthorizeToken(User user)
    {
        // sign JWT with ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        // set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuer(ISSUER)
                .setId(uuidAsString)
                .signWith(signatureAlgorithm, signingKey);

        // add the expiration
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + MILIS * NO_MIN * NO_SECONDS;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        List<String> roles = new ArrayList<>();
        for(Role role:user.getRoles()) {
            roles.add(role.getRoleName());
        }

        // add roles
        builder.claim("roles", roles);

        return builder.compact();
    }

    //for retrieveing any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        try
        {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }
        // pica testul de integritate
        catch (SignatureException signatureException)
        {
            throw new CorruptedJwt("Corrupted JWT!");
        }
    }

    public Claims getAllClaimsFromTokenWithoutValidation(String token) {
        try
        {
            return Jwts.parser().parseClaimsJws(token).getBody();
        }
        // pica testul de integritate
        catch (SignatureException signatureException)
        {
            throw new CorruptedJwt("Corrupted JWT!");
        }
    }

    public boolean isJwtExpired(Claims claims)
    {
        long current_time_seconds = System.currentTimeMillis()/MILIS;
        long exp = Long.parseLong(claims.get("exp").toString());

        return (exp - current_time_seconds) < 0;
    }

    public boolean isIssuerKnown(Claims claims)
    {
        String iss = claims.getIssuer();
        return iss.equals(ISSUER);
    }

    public JSONObject readBlackList() {
        try (FileReader reader = new FileReader(BLACKLIST_PATH)) {
            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(reader);

            return obj;
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addTokenToBlackList(String token) {
        JSONObject jsonObject = readBlackList();
        Claims allClaims = getAllClaimsFromToken(token);
        String jti = (String) allClaims.get("jti");
        if(jti != null)
        {
            jsonObject.put(jti, token);
            writeBlackList(jsonObject);
        }
    }

    public Boolean isTokenBlacklisted(Claims claims)
    {
        JSONObject jsonObject = readBlackList();
        String jti = (String) claims.get("jti");
        if(jsonObject.containsKey(jti))
        {
            return true;
        }
        return false;
    }

    public void writeBlackList(JSONObject obj) {
        try (FileWriter file = new FileWriter(BLACKLIST_PATH))
        {
            file.write(obj.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
