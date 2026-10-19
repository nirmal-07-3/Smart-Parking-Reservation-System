package com.smartparking.service;

import com.smartparking.dao.UserDAO;
import com.smartparking.dao.UserDAOImpl;
import com.smartparking.entity.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

public class UserService {

    private static final int ITERATIONS = 210000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    private final UserDAO userDAO;
    private final SecureRandom secureRandom = new SecureRandom();

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User register(String name, String email, String password,
                         String phone, User.Role role) {

        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || password == null || password.length() < 8
                || phone == null || phone.isBlank()
                || role == null) {
            throw new IllegalArgumentException(
                    "Enter valid details. Password must contain at least 8 characters."
            );
        }

        String normalizedEmail = email.trim().toLowerCase();

        if (userDAO.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException(
                    "An account with this email already exists."
            );
        }

        if (role == User.Role.ADMIN) {
            throw new IllegalArgumentException(
                    "Administrator accounts cannot be created through public registration."
            );
        }

        User user = new User(
                name.trim(),
                normalizedEmail,
                hashPassword(password),
                phone.trim(),
                role,
                User.Status.ACTIVE
        );

        return userDAO.save(user);
    }

    public Optional<User> authenticate(String email, String password) {
        if (email == null || email.isBlank()
                || password == null || password.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> result = userDAO.findByEmail(
                email.trim().toLowerCase()
        );

        if (result.isEmpty()) {
            return Optional.empty();
        }

        User user = result.get();

        if (user.getStatus() != User.Status.ACTIVE) {
            return Optional.empty();
        }

        if (!verifyPassword(password, user.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    private String hashPassword(String password) {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        byte[] hash = deriveKey(password.toCharArray(), salt, ITERATIONS);

        return ITERATIONS + ":"
                + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    private boolean verifyPassword(String password, String storedValue) {
        if (storedValue == null) {
            return false;
        }

        try {
            String[] parts = storedValue.split(":");

            if (parts.length != 3) {
                return false;
            }

            int iterations = Integer.parseInt(parts[0]);

            if (iterations < 100000 || iterations > 1000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[2]);

            if (salt.length < 16 || expectedHash.length != KEY_LENGTH / 8) {
                return false;
            }

            byte[] actualHash = deriveKey(
                    password.toCharArray(), salt, iterations
            );

            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    public User updateProfile(Long userId, String name, String phone) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user account.");
        }

        if (name == null || name.isBlank() || name.trim().length() > 100) {
            throw new IllegalArgumentException("Enter a valid name of at most 100 characters.");
        }

        if (phone == null || !phone.trim().matches("[0-9+() -]{7,15}")) {
            throw new IllegalArgumentException("Enter a valid phone number.");
        }

        User user = userDAO.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User account was not found."));

        user.setName(name.trim());
        user.setPhone(phone.trim());

        return userDAO.update(user);
    }
    public boolean changePassword(
            Long userId,
            String currentPassword,
            String newPassword,
            String confirmPassword) {

        if (userId == null || userId <= 0
                || currentPassword == null
                || newPassword == null
                || confirmPassword == null
                || currentPassword.isBlank()
                || newPassword.isBlank()
                || confirmPassword.isBlank()) {
            throw new IllegalArgumentException(
                    "All password fields are required.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                    "New password and confirmation do not match.");
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters.");
        }

        if (currentPassword.equals(newPassword)) {
            throw new IllegalArgumentException(
                    "New password must differ from the current password.");
        }

        User user = userDAO.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found."));

        if (!verifyPassword(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException(
                    "Current password is incorrect.");
        }

        user.setPassword(hashPassword(newPassword));
        userDAO.update(user);

        return true;
    }

    private byte[] deriveKey(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(
                password, salt, iterations, KEY_LENGTH
        );

        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Password processing failed.", e);
        } finally {
            spec.clearPassword();
        }
    }
}