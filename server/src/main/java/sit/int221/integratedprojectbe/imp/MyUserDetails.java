package sit.int221.integratedprojectbe.imp;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sit.int221.integratedprojectbe.entities.User;
import java.util.List;

@Getter
public class MyUserDetails extends org.springframework.security.core.userdetails.User {
    private final int id;
    private final String email;
    private final String name;
    private final String role;

    public MyUserDetails(User user) {
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getLabel())));
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole().getName();
    }

    public boolean hasRole(String role) {
        SimpleGrantedAuthority myRole = new SimpleGrantedAuthority("ROLE_" + role);
        return getAuthorities().contains(myRole);
    }
}
