package sit.int221.integratedprojectbe.security;

public interface ProviderRegistrar {
    void registerProvider(JwtAuthenticationManagerResolver authenticationManagerResolver);
}
