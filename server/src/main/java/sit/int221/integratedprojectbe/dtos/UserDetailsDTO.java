package sit.int221.integratedprojectbe.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.integratedprojectbe.entities.Role;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
    private Integer id;
    private String name;
    private String email;
    private Role role;
    private Date createdOn;
    private Date updatedOn;


}
