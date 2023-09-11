package com.example.project.MyNeighborhood.user.model;

import com.example.project.MyNeighborhood.chat.model.ChatMessage;
import com.example.project.MyNeighborhood.exception.UserStateException;
import com.example.project.MyNeighborhood.governmentIdentity.model.GovernmentIdentity;
import com.example.project.MyNeighborhood.profilePicture.model.ProfilePicture;
import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.volunteer.model.Volunteer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@Data
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "firstname", length = 50, nullable = false)
    @NotBlank
    private String firstname;
    @Column(name = "lastname", length = 50, nullable = false)
    @NotBlank
    private String lastname;
    @Column(name = "email", length = 250, nullable = false, unique = true)
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank
    private String email;
    @Column(name = "address", length = 250, nullable = false)
    @NotBlank
    private String address;
    @Column(name = "role", length = 250)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "password", length = 250, nullable = false)
    @NotBlank
    private String password;
    @OneToOne
    @JoinColumn(name = "governmentIdentity_id", nullable = false)
    private GovernmentIdentity governmentIdentity;
    @OneToOne
    @JoinColumn(name = "profilePicture_id")
    private ProfilePicture profilePicture;
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Request> requests;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Volunteer> volunteers;
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ChatMessage> chatMessagesSent;
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ChatMessage> chatMessagesReceived;

    public User(final String firstname, final String lastname, final String email, final String address,
                final Role role, final String password, final GovernmentIdentity governmentIdentity) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.role = role;
        this.password = password;
        this.governmentIdentity = governmentIdentity;
        validateState();
    }

    public User(final UUID id, final String firstname, final String lastname, final String email, final String address,
                final Role role, final String password, final GovernmentIdentity governmentIdentity, final ProfilePicture profilePicture,
                final Set<Request> requests, final Set<Volunteer> volunteers, final Set<ChatMessage> chatMessagesSent,
                final Set<ChatMessage> chatMessagesReceived) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.role = role;
        this.password = password;
        this.governmentIdentity = governmentIdentity;
        this.profilePicture = profilePicture;
        this.requests = requests;
        this.volunteers = volunteers;
        this.chatMessagesSent = chatMessagesSent;
        this.chatMessagesReceived = chatMessagesReceived;
        validateState();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private void validateState() {
        assertNotNull(firstname);
        assertNotNull(lastname);
        assertNotNull(email);
        assertNotNull(address);
        assertNotBlank(firstname);
        assertNotBlank(lastname);
        assertNotBlank(email);
        assertNotBlank(address);
    }

    private void assertNotBlank(final String attribute) {
        if (attribute.isBlank()) {
            throw new UserStateException("user.attribute.notBlank", "Attribute should not be blanked");
        }
    }

    private void assertNotNull(final String attribute) {
        if (attribute == null) {
            throw new UserStateException("user.attribute.notNull", "Attribute should not be null");
        }
    }
}
