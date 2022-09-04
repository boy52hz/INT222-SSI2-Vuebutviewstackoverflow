package sit.int221.integratedprojectbe.services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.dtos.*;
import sit.int221.integratedprojectbe.dtos.UserDetailsDTO;
import sit.int221.integratedprojectbe.entities.User;
import sit.int221.integratedprojectbe.entities.UserRole;
import sit.int221.integratedprojectbe.exceptions.ArgumentNotValidException;
import sit.int221.integratedprojectbe.repositories.UserRepository;
import sit.int221.integratedprojectbe.services.imp.UserDetailsServiceImp;
import sit.int221.integratedprojectbe.utils.JwtUtils;
import sit.int221.integratedprojectbe.utils.ListMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    Argon2 argon2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id,
            10,
            10);


    public List<UserDetailsDTO> getUsers() {
        return listMapper.mapList(userRepository.findAllByOrderByUserIdDesc(), UserDetailsDTO.class, modelMapper);
    }

    public UserDetailsDTO getUserById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User ID %s is doesn't exist.", userId)
                ));
        return modelMapper.map(user, UserDetailsDTO.class);
    }

    public User editUser(Integer userId, EditUserDTO updateUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
        User user = userRepository.findById(userId)
                .map(existingCategory -> mapUser(existingCategory, updateUser))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User ID %d doesn't exist.", userId)
                ));
        if (updateUser.getName() != null && userRepository.existsByNameAndUserIdNot(updateUser.getName(), userId)
        ) {
            FieldError error = new FieldError(
                    "editUserDTO",
                    "name",
                    "User name is already exist.");
            bindingResult.addError(error);
        }
        if (updateUser.getEmail() != null && userRepository.existsByEmailAndUserIdNot(updateUser.getEmail(), userId)
        ) {
            FieldError error = new FieldError(
                    "editUserDTO",
                    "email",
                    "Email is already exist.");
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
        return userRepository.saveAndFlush(user);

    }

    public void removeUser(Integer userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User ID %s is doesn't exist", userId)
                ));
        userRepository.deleteById(userId);
    }

    public UserDetailsDTO addNewUser(CreateUserDTO newUser, BindingResult bindingResult) {
        String  argon2Password = argon2.hash(3, 16, 1, newUser.getPassword());
        newUser.setName(newUser.getName().strip());
        newUser.setPassword(argon2Password);


        if(newUser.getRole() == null || newUser.getRole().trim() == ""){

            newUser.setRole("student");
        }

        User user = modelMapper.map(newUser, User.class);
        try {
            UserRole userRole = userRoleService.getRoleByRoleName(newUser.getRole());
            user.setRole(userRole);
        } catch (ResponseStatusException ex) {
            FieldError error = new FieldError(
                    "addUserDTO",
                    "role",
                    ex.getReason());
            bindingResult.addError(error);
        }

        if (newUser.getName() != null && userRepository.existsByName(newUser.getName())
        ) {
            FieldError error = new FieldError(
                    "editUserDTO",
                    "name",
                    "Name is already used.");
            bindingResult.addError(error);
        }
        if (newUser.getEmail() != null && userRepository.existsByEmail(newUser.getEmail())
        ) {
            FieldError error = new FieldError(
                    "editUserDTO",
                    "email",
                    "Email is already used.");
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
        return modelMapper.map(userRepository.saveAndFlush(user), UserDetailsDTO.class);
    }

    public JwtTokenDTO passwordCheck(LoginDTO login , BindingResult bindingResult) {
        User user;
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
      if(login.getEmail() != null && userRepository.existsByEmail(login.getEmail())){
          user = userRepository.findByEmail(login.getEmail().strip());
      }else  {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found.");
      }
      if(!argon2.verify(user.getPassword(), login.getPassword()) )
          throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Password mismatch.");

      try {
          authenticate(user.getEmail(), user.getPassword());
      } catch (Exception ex) {
          System.out.println(ex);
      }
      UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(user.getEmail());
      String jwtToken = jwtUtils.generateToken(userDetails);
      JwtTokenDTO jwtTokenDTO = new JwtTokenDTO("You are now logged in", jwtToken);
      return jwtTokenDTO;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    private User mapUser(User existingUser, EditUserDTO updateUser) {
        if (updateUser.getName() != null) {
            existingUser.setName(updateUser.getName().trim());
        }
        if (updateUser.getEmail() != null) {
            existingUser.setEmail(updateUser.getEmail().trim());
        }
        if (updateUser.getRole() != null) {
            UserRole userRole = userRoleService.getRoleByRoleName(updateUser.getRole());
            existingUser.setRole(userRole);
        }
        return existingUser;
    }
}



