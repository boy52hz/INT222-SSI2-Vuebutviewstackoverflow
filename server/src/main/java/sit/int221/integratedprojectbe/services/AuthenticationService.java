package sit.int221.integratedprojectbe.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.dtos.JwtTokenDTO;
import sit.int221.integratedprojectbe.dtos.LoginDTO;
import sit.int221.integratedprojectbe.dtos.UserDetailsDTO;
import sit.int221.integratedprojectbe.entities.User;
import sit.int221.integratedprojectbe.exceptions.ArgumentNotValidException;
import sit.int221.integratedprojectbe.imp.MyUserDetails;
import sit.int221.integratedprojectbe.repositories.UserRepository;
import sit.int221.integratedprojectbe.services.imp.UserDetailsServiceImp;
import sit.int221.integratedprojectbe.utils.JwtUtils;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtTokenDTO login(LoginDTO login , BindingResult bindingResult) {
        User user;
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
        if(login.getEmail() != null && userRepository.existsByEmail(login.getEmail())){
            user = userRepository.findByEmail(login.getEmail().strip());
        }else  {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found.");
        }
        if(!argon2PasswordEncoder.matches(login.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Password mismatch.");

        Authentication authentication = null;
        
        try {
            authentication = authenticate(user.getEmail(), login.getPassword());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        Jwt accessToken = jwtUtils.createAccessToken(userDetails);
        Jwt refreshToken = jwtUtils.createRefreshToken(userDetails);
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO("Login successful", accessToken.getTokenValue(), refreshToken.getTokenValue());
        return jwtTokenDTO;
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

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
