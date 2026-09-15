package cuet.shcms.app.controller;

import cuet.shcms.app.dto.LoginRequest;
import cuet.shcms.app.dto.LoginResponse;
import cuet.shcms.app.entity.User;
import cuet.shcms.app.security.JwtUtil;
import cuet.shcms.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userService.authenticate(request.getUsername(), request.getPassword());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = jwtUtil.generateToken(user.getUsername(), user.getUserId(), user.getRole().name());

            LoginResponse response = new LoginResponse(
                token,
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getRole().name().toLowerCase(),
                user.getEmail(),
                user.getRoom(),
                user.getDepartment(),
                user.getPhone(),
                user.getActive()
            );

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("{\"error\": \"Invalid credentials\"}");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("{\"message\": \"Logged out successfully\"}");
    }
}