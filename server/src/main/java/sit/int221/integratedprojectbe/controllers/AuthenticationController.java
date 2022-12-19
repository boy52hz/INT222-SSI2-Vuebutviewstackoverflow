package sit.int221.integratedprojectbe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.dtos.*;
import sit.int221.integratedprojectbe.imp.MyUserDetails;
import sit.int221.integratedprojectbe.security.AuthenticationUtil;
import sit.int221.integratedprojectbe.security.TokenGenerator;
import sit.int221.integratedprojectbe.services.AuthenticationService;
import sit.int221.integratedprojectbe.services.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationUtil authenticationUtil;
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
    public UserDetailsDTO loadUser(Authentication authentication) {
        MyUserDetails myUserDetails = authenticationUtil.getUserDetail(authentication);

        try {
            return authenticationService.loadUserDetailByEmail(myUserDetails.getUsername());
        } catch (NullPointerException ex) {
            System.out.println(myUserDetails);
        }
        return null;
    }

    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity verifyAccessToken (@RequestParam("accessToken") String accessToken) {
            Boolean validated = authenticationService.verifyAccessToken(accessToken);
            if (!validated) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
            }
            return ResponseEntity.ok().build();
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
    @ResponseStatus(HttpStatus.CREATED)
    public JwtTokenDTO refreshAndGetAuthenticationToken(@RequestBody JwtTokenDTO jwtTokenDTO) {
        Authentication authentication = jwtRefreshTokenAuthProvider.authenticate(
                new BearerTokenAuthenticationToken(jwtTokenDTO.getRefreshToken()));
        return tokenGenerator.createToken(authentication);
    }
}
