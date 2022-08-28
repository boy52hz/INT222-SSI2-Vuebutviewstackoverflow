package sit.int221.integratedprojectbe.services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.dtos.*;
import sit.int221.integratedprojectbe.dtos.UserDetailsDTO;
import sit.int221.integratedprojectbe.entities.Event;
import sit.int221.integratedprojectbe.entities.EventCategory;
import sit.int221.integratedprojectbe.entities.Role;
import sit.int221.integratedprojectbe.entities.User;
import sit.int221.integratedprojectbe.exceptions.ArgumentNotValidException;
import sit.int221.integratedprojectbe.exceptions.DateTimeOverlapException;
import sit.int221.integratedprojectbe.repositories.UserRepository;
import sit.int221.integratedprojectbe.utils.ListMapper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    Argon2 argon2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id,
            10,
            10);


    public List<UserDetailsDTO> getUsers() {
        return listMapper.mapList(userRepository.findAllByOrderByNameAsc(), UserDetailsDTO.class, modelMapper);
    }

    public UserDetailsDTO getUserById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Booking ID %s is doesn't exist.", userId)
                ));
        return modelMapper.map(user, UserDetailsDTO.class);
    }

    public User editUser(Integer userId, ManageUserDTO updateUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
        User user = userRepository.findById(userId)
                .map(existingCategory -> mapUser(existingCategory, updateUser))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Category ID %d doesn't exist.", userId)
                ));
        if (updateUser.getName() != null && userRepository.existsByNameAndUserIdNot(updateUser.getName(), userId)
        ) {
            FieldError error = new FieldError(
                    "editUserDTO",
                    "Name",
                    "User name is already exist.");
            bindingResult.addError(error);
        }
        if (updateUser.getEmail() != null && userRepository.existsByEmailAndUserIdNot(updateUser.getEmail(), userId)
        ) {
            FieldError error = new FieldError(
                    "editUserDTO",
                    "Email",
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
                        String.format("Booking ID %s is doesn't exist", userId)
                ));
        userRepository.deleteById(userId);
    }

    public UserDetailsDTO addNewUser(ManageUserDTO newUser, BindingResult bindingResult) {
        String  argon2Password = argon2.hash(22, 65536, 1, newUser.getPassword());
        newUser.setName(newUser.getName().strip());
        newUser.setEmail(newUser.getEmail().strip());
        newUser.setPassword(argon2Password);
        if(newUser.getRole() == null || Objects.equals(newUser.getRole(), "")){
            newUser.setRole(String.valueOf(Role.student));
        }
        User user = modelMapper.map(newUser,User.class);
        if (newUser.getName() != null && userRepository.existsByName(newUser.getName())
        ) {
            FieldError error = new FieldError(
                    "editUserDTO",
                    "Name",
                    "User name is already exist.");
            bindingResult.addError(error);
        }
        if (newUser.getEmail() != null && userRepository.existsByEmail(newUser.getEmail())
        ) {
            FieldError error = new FieldError(
                    "editUserDTO",
                    "Email",
                    "Email is already exist.");
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
        return modelMapper.map(userRepository.saveAndFlush(user), UserDetailsDTO.class);
    }

    public UserDetailsDTO passwordCheck(LoginDTO login , BindingResult bindingResult){
        User user = new User() ;
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
      if(login.getEmail() != null && userRepository.existsByEmail(login.getEmail())){
          user = userRepository.findByEmail(login.getEmail().strip());
      }else  {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email Not Found !!");
      }
      if(!argon2.verify(user.getPassword(), login.getPassword()) )
          throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Password mismatch");


      return modelMapper.map(user,UserDetailsDTO.class);

    }





    private User mapUser(User existingUser, ManageUserDTO updateUser) {
        if (updateUser.getName() != null) {
            existingUser.setName(updateUser.getName().trim());
        }
        if (updateUser.getEmail() != null) {
            existingUser.setEmail(updateUser.getEmail().trim());
        }
        if (updateUser.getRole() != null) {
            existingUser.setRole(Role.valueOf(updateUser.getRole()));
        }
        return existingUser;

    }







    }



