package sit.int221.integratedprojectbe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sit.int221.integratedprojectbe.dtos.*;
import sit.int221.integratedprojectbe.imp.MyUserDetails;
import sit.int221.integratedprojectbe.security.TokenGenerator;
import sit.int221.integratedprojectbe.services.AuthenticationService;
import sit.int221.integratedprojectbe.services.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    JwtAuthenticationProvider jwtRefreshTokenAuthProvider;

    @Autowired
    TokenGenerator tokenGenerator;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDTO loadUser(@AuthenticationPrincipal MyUserDetails myUerDetails) {
        try {
            return authenticationService.loadUserDetailByEmail(myUerDetails.getUsername());
        } catch (NullPointerException ex) {
            System.out.println(myUerDetails);
        }
        return null;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public JwtTokenDTO login(@Valid @RequestBody LoginDTO newUser , BindingResult bindingResult) {
        return authenticationService.login(newUser, bindingResult);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailsDTO create(@Valid @RequestBody CreateUserDTO newUser, BindingResult bindingResult) {
        return userService.addNewUser(newUser, bindingResult);
    }

    @PostMapping("/refreshToken")
    public JwtTokenDTO refreshAndGetAuthenticationToken(@RequestBody JwtTokenDTO jwtTokenDTO) {
        Authentication authentication = jwtRefreshTokenAuthProvider.authenticate(
                new BearerTokenAuthenticationToken(jwtTokenDTO.getRefreshToken()));
        return tokenGenerator.createToken(authentication);
    }
}
