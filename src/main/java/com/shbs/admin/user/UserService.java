package com.shbs.admin.user;

import com.shbs.admin.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Iterable<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByUsername(String name) {
        return userRepository.findOne(name);
    }

    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean isPasswordMatch(final String currentPassword, final String savedPassword) {
        return passwordEncoder.matches(currentPassword, savedPassword);
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void deleteUser(String name) {
        userRepository.delete(name);
    }
}
