package sit.int221.integratedprojectbe.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.dtos.JwtTokenDTO;
import sit.int221.integratedprojectbe.dtos.LoginDTO;
import sit.int221.integratedprojectbe.dtos.UserDetailsDTO;
import sit.int221.integratedprojectbe.entities.User;
import sit.int221.integratedprojectbe.exceptions.ArgumentNotValidException;
import sit.int221.integratedprojectbe.repositories.UserRepository;
import sit.int221.integratedprojectbe.security.JwtAuthenticationManagerResolver;
import sit.int221.integratedprojectbe.security.TokenGenerator;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TokenGenerator tokenGenerator;
    @Autowired
    DaoAuthenticationProvider authenticateProvider;
    @Autowired
    JwtDecoder jwtDecoder;

    public JwtTokenDTO login(LoginDTO login, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }

        try {
            Authentication authentication = authenticateProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
            return tokenGenerator.createToken(authentication);
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password mismatch");
        } catch (UsernameNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    public UserDetailsDTO loadUserDetailByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Email %s is doesn't exist.", email)
            );
        }
        return modelMapper.map(user, UserDetailsDTO.class);
    }

    public Boolean verifyAccessToken(String accessToken) {
        try {
            jwtDecoder.decode(accessToken);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }
}
