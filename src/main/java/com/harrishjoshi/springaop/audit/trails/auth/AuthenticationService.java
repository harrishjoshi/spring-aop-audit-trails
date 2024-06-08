package com.harrishjoshi.springaop.audit.trails.auth;

import com.harrishjoshi.springaop.audit.trails.config.JwtService;
import com.harrishjoshi.springaop.audit.trails.helper.AppContext;
import com.harrishjoshi.springaop.audit.trails.helper.ContextKey;
import com.harrishjoshi.springaop.audit.trails.token.Token;
import com.harrishjoshi.springaop.audit.trails.token.TokenRepository;
import com.harrishjoshi.springaop.audit.trails.token.TokenType;
import com.harrishjoshi.springaop.audit.trails.user.User;
import com.harrishjoshi.springaop.audit.trails.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = repository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + request.email() + " not found."));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        AppContext.set(ContextKey.USER_ID, user.getId());
        AppContext.set(ContextKey.EMAIL, user.getEmail());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request
    ) throws Exception {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith(TokenType.BEARER.value())) {
            throw new Exception("Invalid refresh token.");
        }

        refreshToken = authHeader.substring(7);
        try {
            userEmail = jwtService.extractUsername(refreshToken);
        } catch (Exception e) {
            throw new Exception("Invalid refresh token.");
        }

        if (userEmail == null) {
            throw new Exception("Invalid refresh token.");
        }

        var user = this.repository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user details."));
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new Exception("Invalid refresh token.");
        }

        var accessToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }
}