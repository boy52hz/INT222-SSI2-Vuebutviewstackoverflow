package sit.int221.integratedprojectbe.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int221.integratedprojectbe.dtos.UserRoleDTO;
import sit.int221.integratedprojectbe.entities.UserRole;
import sit.int221.integratedprojectbe.services.UserRoleService;
import sit.int221.integratedprojectbe.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("")
    public List<UserRoleDTO> getAllRoles(){
        return listMapper.mapList(userRoleService.getAllRoles(), UserRoleDTO.class, modelMapper);
    }

    @GetMapping("/registerable")
    public List<UserRoleDTO> getRegisterableRoles() {
        return listMapper.mapList(userRoleService.getRegisterableRoles(), UserRoleDTO.class, modelMapper);
    }

    @GetMapping("/{roleName}")
    private UserRoleDTO getRoleByRoleName(@PathVariable String roleName) {
        return modelMapper.map(userRoleService.getRoleByRoleName(roleName), UserRoleDTO.class);
    }
}
