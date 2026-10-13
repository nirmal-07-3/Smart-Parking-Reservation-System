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