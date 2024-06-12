package com.example.CommunistDate.Users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.token-validity}")
    private long jwtLength;

    @Value("${security.jwt.token-issuer}")
    private String jwtIssuer;

    public String createJwtToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(user.getUsername())
            .issuer(jwtIssuer)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(jwtLength))
            .claim("is_admin", user.getIsAdmin())
            .build();

        var encoder = new NimbusJwtEncoder(
            new ImmutableSecret<>(jwtSecretKey.getBytes()));
        var params = JwtEncoderParameters.from(
            JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return encoder.encode(params).getTokenValue();
    }
}

