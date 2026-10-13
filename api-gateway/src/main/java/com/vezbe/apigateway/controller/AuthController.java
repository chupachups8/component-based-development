package com.vezbe.apigateway.controller;

import com.vezbe.apigateway.dto.LoginRequest;
import com.vezbe.apigateway.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthController(
            ReactiveUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

        @PostMapping("/login")
        public Mono<ResponseEntity<LoginResponse>> login(
                @RequestBody LoginRequest request) {

            return userDetailsService
                    .findByUsername(request.getUsername())

                    .filter(user ->
                            passwordEncoder.matches(
                                    request.getPassword(),
                                    user.getPassword()
                            )
                    )

                    .map(user -> {

                        String role = user.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .map(authority ->
                                        authority.replace("ROLE_", "")
                                )
                                .findFirst()
                                .orElse("USER");

                        Instant now = Instant.now();

                        JwtClaimsSet claims = JwtClaimsSet.builder()
                                .issuer("api-gateway")
                                .subject(user.getUsername())
                                .issuedAt(now)
                                .expiresAt(now.plusSeconds(3600))
                                .claim("role", role)
                                .build();

                        JwsHeader header = JwsHeader
                                .with(MacAlgorithm.HS256)
                                .build();

                        String token = jwtEncoder
                                .encode(
                                        JwtEncoderParameters.from(
                                                header,
                                                claims
                                        )
                                )
                                .getTokenValue();

                        return ResponseEntity.ok(
                                new LoginResponse(token, role)
                        );
                    })

                    .switchIfEmpty(
                            Mono.just(
                                    ResponseEntity
                                            .status(HttpStatus.UNAUTHORIZED)
                                            .build()
                            )
                    );
        }
}
