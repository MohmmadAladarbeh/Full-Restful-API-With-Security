package com.example.restfulapisecurity.auth;

import com.example.restfulapisecurity.model.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class will be used for creating & resolving jwt tokens.
 */


@Component
public class JwtUtil {

    private final String SECRET_KEY = "mysecretkey";
    private long accessTokenValidity = 60 * 60 * 1000;
    private final JwtParser jwtParser;
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(SECRET_KEY);
    }


    /**
     * this method is responsible for creating a JWT containing the user's identity and additional information (claims),
     * signing it with a secret key, and returning the resulting token as a string.
     * This token can then be used for authentication and authorization purposes in a web application
     */
    public String createToken (User user) {

        /**
         * 'Claims' object, which represents the payload of the JWT.
         * The subject of the JWT is set to the user's email,
         * and additional custom claims for the user's first name and last name are added to the payload.
         */
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());

        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));

        /**
         * we are using the Jwts.builder() to build JWT
         */
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * this method is responsible for parsing the JWT token string, verifying its signature,
     * and returning the claims contained within the token. These claims typically include information about the subject,
     * expiration time, and any custom data associated with the token.
     */
    private Claims parseJwtClaims (String token) {
        /**parseClaimsJws(token).getBody()
         * 1. This method parses the token string and verifies its signature using the provided jwtParser.
         * 2. It retrieves the body of the parsed JWT, which contains the claims (payload) of the token.
         *      getBody(): This method returns the claims of the parsed JWT token.
         * 2. Finally, it returns the parsed claims ('Claims' object).
         */
        return jwtParser.parseClaimsJws(token).getBody();
    }

    /**
     * The resolveClaims() method resolves and parses JWT claims from an incoming HTTP request,
     */
    public Claims resolveClaims (HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null){
                return parseJwtClaims(token);
            }
            return null;
        }catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        }catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken (HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null
                && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * The resolveToken() method extracts the JWT token from the request header.
     *  1. This method retrieves the JWT token from the HTTP request header.
     *  2. It checks if the request contains the "Authorization" header (TOKEN_HEADER) and if it starts with the expected token prefix (TOKEN_PREFIX).
     *  3.If the header is present and starts with the correct prefix, it returns the extracted token; otherwise, it returns null.
     */
    public boolean validateClaims (Claims claims) throws AuthenticationException {
        try{
            return claims.getExpiration().after(new Date());
        }catch (Exception ex) {
            throw ex;
        }
    }

    public String getEmail (Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles (Claims claims) {
        return (List<String>) claims.get("roles");
    }

}
