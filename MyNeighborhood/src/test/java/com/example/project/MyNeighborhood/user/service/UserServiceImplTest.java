package com.example.project.MyNeighborhood.user.service;

import com.example.project.MyNeighborhood.exception.EmailAlreadyExistsException;
import com.example.project.MyNeighborhood.exception.UserDoesNotExistException;
import com.example.project.MyNeighborhood.user.model.Role;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.model.UserDTO;
import com.example.project.MyNeighborhood.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String EMAIL = "nicky@larson.jp";

    private UserServiceImpl userServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper = new UserDTOMapper();

    @BeforeEach
    void setUp() {
        Assertions.assertThat(userRepository).isNotNull();
        Assertions.assertThat(passwordEncoder).isNotNull();
        userServiceImpl = new UserServiceImpl(userRepository, userDTOMapper, passwordEncoder);
    }

    @Test
    void getUserById() {
        //Given
        final User user = buildUser();
        final UserDTO userDTO = userDTOMapper.apply(user);
        //When
        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        final UserDTO result = userServiceImpl.getUserById(ID.toString());
        //Then
        Assertions.assertThat(result).isNotNull()
                .isEqualTo(userDTO);
        Mockito.verify(userRepository, Mockito.times(1)).findById(ID);
    }

    @Test
    void getUserByIdWhenUserDoesNotExist() {
        //Given && When
        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.empty());
        //Then
        Assertions.assertThatThrownBy(() -> userServiceImpl.getUserById(ID.toString()))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessage("This user doesn't exist");
    }

    @Test
    void getUserByEmail() {
        //Given
        final User user = buildUser();
        //When
        Mockito.when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
        final User result = userServiceImpl.getUserByEmail(EMAIL);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(user);
        Mockito.verify(userRepository, Mockito.times(1)).findUserByEmail(EMAIL);
    }

    @Test
    void getUserByEmailWhenUserDoesNotExist() {
        //Given && When
        Mockito.when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
        //Then
        Assertions.assertThatThrownBy(() -> userServiceImpl.getUserByEmail(EMAIL))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void createUser() {
        //Given
        final User user = buildUser();
        //When
        Mockito.when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
        userServiceImpl.createUser(user);
        //Then
        Mockito.verify(userRepository, Mockito.times(1)).findUserByEmail(EMAIL);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void createUserWhenEmailAlreadyExists() {
        //Given
        final User user = buildUser();
        //When
        Mockito.when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
        //Then
        Assertions.assertThatThrownBy(() -> userServiceImpl.createUser(user))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("This email already exist");
        Mockito.verify(userRepository, Mockito.times(1)).findUserByEmail(EMAIL);
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void updateUser() {
        //Given
        final User user = buildUser();
        final UUID newId = UUID.randomUUID();
        final User newUser = User.builder()
                .id(newId)
                .address("address2")
                .firstname("Saeko")
                .lastname("Nogami")
                .role(Role.USER)
                .email("saeko@nogami.jp")
                .password(passwordEncoder.encode("ryo"))
                .profilePicture(null)
                .governmentIdentity(null)
                .build();
        //When
        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        userServiceImpl.updateUser(ID, newUser);
        //Then
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getFirstname()).isEqualTo(newUser.getFirstname());
        Assertions.assertThat(user.getLastname()).isEqualTo(newUser.getLastname());
        Assertions.assertThat(user.getPassword()).isEqualTo(newUser.getPassword());
        Assertions.assertThat(user.getAddress()).isEqualTo(newUser.getAddress());
        Assertions.assertThat(user.getEmail()).isEqualTo(newUser.getEmail());
        Assertions.assertThat(user.getGovernmentIdentity()).isEqualTo(newUser.getGovernmentIdentity());
        Assertions.assertThat(user.getProfilePicture()).isEqualTo(newUser.getProfilePicture());
        Mockito.verify(userRepository, Mockito.times(1)).findById(ID);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    private User buildUser() {
        return User.builder()
                .id(ID)
                .firstname("Nicky")
                .lastname("Larson")
                .email(EMAIL)
                .role(Role.USER)
                .password(passwordEncoder.encode("kaori"))
                .address("address")
                .governmentIdentity(null)
                .profilePicture(null)
                .build();
    }
}
