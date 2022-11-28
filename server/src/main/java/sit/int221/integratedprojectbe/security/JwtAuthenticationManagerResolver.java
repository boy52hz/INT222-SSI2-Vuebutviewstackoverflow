package sit.int221.integratedprojectbe.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {
    private final Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

    JwtIssuerAuthenticationManagerResolver authenticationManagerResolver =
            new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);

    public void addAuthenticationManager (String issuer, JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.authenticationManagers.put(issuer, jwtAuthenticationProvider::authenticate);
    }

    public void addAuthenticationManager (String issuer) {
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(JwtDecoders.fromIssuerLocation(issuer));
        this.authenticationManagers.put(issuer, jwtAuthenticationProvider::authenticate);
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest context) {
        return authenticationManagerResolver.resolve(context);
    }

    public void register(ProviderRegistrar providerRegistrar) {
        providerRegistrar.registerProvider(this);
    }
}
