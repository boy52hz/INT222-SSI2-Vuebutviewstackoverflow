package sit.int221.integratedprojectbe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sit.int221.integratedprojectbe.dtos.*;
import sit.int221.integratedprojectbe.entities.User;
import sit.int221.integratedprojectbe.services.AuthenticationService;
import sit.int221.integratedprojectbe.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/login")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDTO loadUser(@AuthenticationPrincipal UserDetails userDetails) {
        return authenticationService.loadUserDetailByEmail(userDetails.getUsername());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public JwtTokenDTO login(@Valid @RequestBody LoginDTO newUser , BindingResult bindingResult) {
        return authenticationService.login(newUser, bindingResult);
    }
}
