package sit.int221.integratedprojectbe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sit.int221.integratedprojectbe.dtos.*;
import sit.int221.integratedprojectbe.entities.User;
import sit.int221.integratedprojectbe.services.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtTokenDTO matchUser(@Valid @RequestBody LoginDTO newUser , BindingResult bindingResult) {
        return userService.passwordCheck(newUser, bindingResult);
    }

}
