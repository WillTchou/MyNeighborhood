package com.example.project.MyNeighborhood.user.service;

import com.example.project.MyNeighborhood.exception.EmailAlreadyExistsException;
import com.example.project.MyNeighborhood.exception.UserDoesNotExistException;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.model.UserDTO;
import com.example.project.MyNeighborhood.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepository userRepository, final UserDTOMapper userDTOMapper,
                           final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO getUserById(final String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .map(userDTOMapper)
                .orElseThrow(UserDoesNotExistException::new);
    }

    @Override
    public User getUserByEmail(final String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void createUser(final User user) {
        final String userEmail = user.getEmail();
        final String message = "This email already exist";
        final Optional<User> optionalUser = userRepository.findUserByEmail(userEmail);
        if (optionalUser.isPresent()) {
            throw new EmailAlreadyExistsException(message);
        }
        userRepository.save(user);
    }

    @Override
    public void updateUser(final UUID userId, final User updatedUser) {
        userRepository.findById(userId)
                .ifPresent(userToUpdate -> {
                    setUserFields(userToUpdate, updatedUser);
                    userRepository.save(userToUpdate);
                });
    }

    private void setUserFields(final User userToUpdate, final User updatedUser) {
        final String updatedUserPassword = updatedUser.getPassword();
        userToUpdate.setEmail(updatedUser.getEmail());
        userToUpdate.setPassword(passwordEncoder.encode(updatedUserPassword));
        userToUpdate.setFirstname(updatedUser.getFirstname());
        userToUpdate.setLastname(updatedUser.getLastname());
        userToUpdate.setAddress(updatedUser.getAddress());
        userToUpdate.setGovernmentIdentity(updatedUser.getGovernmentIdentity());
        userToUpdate.setProfilePicture(updatedUser.getProfilePicture());
    }
}
