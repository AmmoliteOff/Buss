package org.hackathon.buss.auth;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.hackathon.buss.util.view.NonDetailedInformation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @JsonView(NonDetailedInformation.class)
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,
                String.valueOf(response.getToken())).body(response.getUser());
    }

    @PostMapping("/login")
    @JsonView(NonDetailedInformation.class)
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,
                String.valueOf(response.getToken())).body(response.getUser());
    }
}
