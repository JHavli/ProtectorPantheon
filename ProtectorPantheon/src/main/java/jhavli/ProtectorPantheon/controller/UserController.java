package jhavli.ProtectorPantheon.controller;

import jhavli.ProtectorPantheon.mapper.UserRepository;
import jhavli.ProtectorPantheon.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and #id == principal.id")
    public User updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/password")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public User updateOwnPassword(@RequestBody String newPassword) {
        User currentUser = getCurrentUser();
        currentUser.setPassword(newPassword);
        return userRepository.save(currentUser);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
}
