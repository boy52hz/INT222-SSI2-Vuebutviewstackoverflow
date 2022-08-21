package sit.int221.integratedprojectbe.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.integratedprojectbe.entities.Role;
import sit.int221.integratedprojectbe.exceptions.Enummismatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManageUserDTO {
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 100, message = "size must be between 1 and 100")
    private String name;

    @NotBlank(message = "must not be blank")
    @Email(regexp = "^[^(.)][a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,3}",
            message = "Email must be well formed")
    @Size(min = 1, max =50 , message = "size must be between 1 and 50")
    private String email;
    @Enummismatch(enumClass = Role.class)
    @NotNull(message = "must not be null")
    private String role;

}
