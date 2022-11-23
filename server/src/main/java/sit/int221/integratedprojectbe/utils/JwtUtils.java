package sit.int221.integratedprojectbe.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sit.int221.integratedprojectbe.imp.MyUserDetails;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component

public class JwtUtils {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.accessToken.expiration.in.seconds}")
    private Long accessTokenExpiration;

    @Value("${jwt.refreshToken.expiration.in.seconds}")
    private Long refreshTokenExpiration;

    public Jwt generateToken(MyUserDetails userDetails, Long maxAgeSeconds) {
        Instant expiresAt = Instant.now().plusSeconds(maxAgeSeconds);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://intproj21.sit.kmutt.ac.th/ssi2")
                .subject(userDetails.getEmail())
                .expiresAt(expiresAt)
                .issuedAt(Instant.now()).build();

        return encodeTokenWithDefaultHeaders(claims);
    }

    public Jwt createAccessToken(MyUserDetails userDetails) {
        return generateToken(userDetails, accessTokenExpiration);
    }

    public Jwt createRefreshToken(MyUserDetails userDetails) {
        return generateToken(userDetails, refreshTokenExpiration);
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }
    public JwtEncoder Encoder () {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(secretKey);
        return new NimbusJwtEncoder(immutableSecret);
    }

    public JwtDecoder Decoder () {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
        OAuth2TokenValidator<Jwt> withoutClockSkew = new JwtTimestampValidator(Duration.ofSeconds(0));
        decoder.setJwtValidator(withoutClockSkew);
        return decoder;
    }

    private Jwt encodeTokenWithDefaultHeaders(JwtClaimsSet claims) {
        JwtEncoder encoder = Encoder();
        return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims));
    }

    public Jwt decode(String token) throws JwtException {
        JwtDecoder decoder = Decoder();
        return decoder.decode(token);
    }
}
