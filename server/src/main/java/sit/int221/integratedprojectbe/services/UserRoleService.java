package sit.int221.integratedprojectbe.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.dtos.UserRoleDTO;
import sit.int221.integratedprojectbe.entities.UserRole;
import sit.int221.integratedprojectbe.repositories.UserRoleRepository;
import sit.int221.integratedprojectbe.utils.ListMapper;

import java.util.List;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<UserRole> getRegisterableRoles() {
        return userRoleRepository.findAllByRegisterable(true);
    }

    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

    public UserRole getRoleByRoleName(String roleName) {
        return userRoleRepository.findById(roleName).orElseThrow(() ->
            new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Role name %s is doesn't exist.", roleName)
            ));
    }
}
