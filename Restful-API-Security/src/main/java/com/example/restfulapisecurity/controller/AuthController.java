package com.example.restfulapisecurity.controller;


import com.example.restfulapisecurity.auth.JwtUtil;
import com.example.restfulapisecurity.model.User;
import com.example.restfulapisecurity.model.request.LoginReq;
import com.example.restfulapisecurity.model.response.ErrorRes;
import com.example.restfulapisecurity.model.response.LoginRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping ("/rest/auth")
public class AuthController {

    /**
     * 'The AuthenticationManager' interface in Spring Security is responsible for authenticating an Authentication object,
       typically representing a user's credentials.
       It is a core component of the authentication process in Spring Security.
     */
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * . It expects a JSON request body containing the email and password (LoginReq object).
     * It attempts to authenticate the user using the AuthenticationManager. If authentication succeeds,
     * it generates a JWT token using the JwtUtil and constructs a LoginRes object containing the user's email and the token.
     * It then returns a 200 OK response with the login response. If authentication fails due to bad credentials,
     * it returns a 400 Bad Request response with an error message.
     * */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "/login")
    public ResponseEntity login(@RequestBody LoginReq loginReq) {
        try{
            /** Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
             * This line authenticate the user with email & password. The authenticationManager.authenticate() method will
             * internally call loadUserByUsername() method from our CustomUserDetailsService class. T
             * hen it will match the password from userDetailsService with the password found from LoginReq.
             * This method will throw exception if the authentication is not successful.
             */
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();
            User user = new User(email, "");
            String token = jwtUtil.createToken(user);
            LoginRes loginRes = new LoginRes(email, token);

            return ResponseEntity.ok(loginRes);
        }catch (BadCredentialsException e){

            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){

            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
