package sit.int221.integratedprojectbe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import sit.int221.integratedprojectbe.imp.MyUserDetails;
import sit.int221.integratedprojectbe.services.imp.UserDetailsServiceImp;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    @Autowired
    UserDetailsServiceImp userDetailsServiceImp;

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        MyUserDetails myUserDetails = userDetailsServiceImp.loadUserByUsername(jwt.getSubject());
        return new UsernamePasswordAuthenticationToken(myUserDetails, jwt, myUserDetails.getAuthorities());
    }
}
