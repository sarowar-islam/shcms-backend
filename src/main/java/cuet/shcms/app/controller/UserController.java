package cuet.shcms.app.controller;

import cuet.shcms.app.dto.UserRequest;
import cuet.shcms.app.entity.User;
import cuet.shcms.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        if (request.getRole() == null || request.getRole() == User.Role.ADMIN) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only student and staff accounts can be managed here."));
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password is required."));
        }
        if (userService.existsByUsername(request.getUsername()) || userService.existsByEmail(request.getEmail())
                || userService.findByUserId(request.getUserId()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User ID, username, or email already exists."));
        }

        User user = new User();
        apply(request, user, true);
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserRequest request) {
        return userService.findByUserId(userId).map(user -> {
            if (request.getRole() == User.Role.ADMIN) {
                return ResponseEntity.badRequest().body(Map.of("error", "Admin accounts cannot be changed here."));
            }
            if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())
                    && userService.existsByUsername(request.getUsername())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username already exists."));
            }
            if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())
                    && userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already exists."));
            }
            apply(request, user, false);
            return ResponseEntity.ok(userService.saveUser(user));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String userId, @RequestBody Map<String, Boolean> body) {
        return userService.findByUserId(userId).map(user -> {
            user.setActive(Boolean.TRUE.equals(body.get("active")));
            return ResponseEntity.ok(userService.saveUser(user));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        return userService.findByUserId(userId).map(user -> {
            if (user.getRole() == User.Role.ADMIN) {
                return ResponseEntity.badRequest().body(Map.of("error", "Admin accounts cannot be deleted here."));
            }
            userService.deleteByUserId(userId);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private void apply(UserRequest request, User user, boolean creating) {
        user.setUserId(request.getUserId());
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRoom(request.getRoom());
        user.setDepartment(request.getDepartment());
        user.setPhone(request.getPhone());
        user.setActive(request.getActive() == null || request.getActive());
        if (creating) user.setJoinedDate(LocalDate.now());
        if (request.getPassword() != null && !request.getPassword().isBlank()) user.setPassword(request.getPassword());
    }
}