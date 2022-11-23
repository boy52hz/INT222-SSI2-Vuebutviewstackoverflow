package sit.int221.integratedprojectbe.controllers;

import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.dtos.*;
import sit.int221.integratedprojectbe.imp.MyUserDetails;
import sit.int221.integratedprojectbe.services.AuthenticationService;
import sit.int221.integratedprojectbe.services.UserService;
import sit.int221.integratedprojectbe.services.imp.UserDetailsServiceImp;
import sit.int221.integratedprojectbe.utils.JwtUtils;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private String token;
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtils   jwtUtils;

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    private UserService userService;


    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDTO loadUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        try {
            return authenticationService.loadUserDetailByEmail(userDetails.getEmail());
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
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

    @GetMapping("/refreshToken")
    public ResponseEntity<AccessTokenDTO>refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        final String token = authToken.substring(7);
        String username =  jwtUtils.Decoder().decode(token).getSubject();

        MyUserDetails userDetails = (MyUserDetails) userDetailsServiceImp.loadUserByUsername(username);

        if (userDetails != null) {
            Jwt accessToken = jwtUtils.createAccessToken(userDetails);
            return ResponseEntity.ok(new AccessTokenDTO("refreshed", accessToken.getTokenValue()));
        }
        else {
            return ResponseEntity.status(401).build();
        }
    }
}
