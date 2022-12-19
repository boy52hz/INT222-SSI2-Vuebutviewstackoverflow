package sit.int221.integratedprojectbe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import sit.int221.integratedprojectbe.entities.User;
import sit.int221.integratedprojectbe.entities.UserRole;
import sit.int221.integratedprojectbe.imp.MyUserDetails;

@Component
public class AuthenticationUtil {
    @Autowired
    AadResourceServerUtils aadResourceServerUtils;
    public MyUserDetails getUserDetail(Authentication authentication) {
        MyUserDetails myUserDetails = null;

        if (authentication instanceof JwtAuthenticationToken) {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            if (aadResourceServerUtils.isAadToken(jwt)) {
                String aadRole = jwt.getClaimAsStringList("roles").get(0);

                User user = new User();
                user.setName(jwt.getClaimAsString("name"));
                user.setEmail(jwt.getClaimAsString("preferred_username"));

                UserRole role = new UserRole();
                role.setName(aadRole.toLowerCase());
                role.setLabel(aadRole);
                user.setRole(role);
                System.out.println(aadRole);
                myUserDetails = new MyUserDetails(user);
            }
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            myUserDetails = (MyUserDetails) authentication.getPrincipal();
        }
        return myUserDetails;
    }
}
