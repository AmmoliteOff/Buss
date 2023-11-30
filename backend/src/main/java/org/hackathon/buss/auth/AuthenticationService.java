package org.hackathon.buss.auth;


import lombok.RequiredArgsConstructor;
import org.hackathon.buss.enums.Role;
import org.hackathon.buss.model.Dispatcher;
import org.hackathon.buss.model.Driver;
import org.hackathon.buss.model.User;
import org.hackathon.buss.service.UserService;
import org.hackathon.buss.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getUserRole())
                .build();

        if(user.getRole() == Role.DISPATCHER) {
            user = new Dispatcher(user);
            userService.save(user);
        } else {
            user = new Driver(user);
            userService.save(user);
        }

        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userService.findByUsername(request.getUsername()).orElseThrow();


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }
}
