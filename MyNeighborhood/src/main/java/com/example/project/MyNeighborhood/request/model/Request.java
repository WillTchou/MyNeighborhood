package com.example.project.MyNeighborhood.request.model;

import com.example.project.MyNeighborhood.exception.RequestStateException;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.volunteer.model.Volunteer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "type", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(name = "status", columnDefinition = "varchar(50) default 'Unfulfilled'", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.Unfulfilled;
    @Column(name = "latitude", nullable = false)
    private Float latitude;
    @Column(name = "longitude", nullable = false)
    private Float longitude;
    @Column(name = "description", length = 300, nullable = false)
    @NotBlank(message = "Description is mandatory")
    private String description;
    @Column(name = "creation_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate = LocalDateTime.now();
    @Column(name = "fulfilled_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fulfilledDate;
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Volunteer> volunteers;

    public Request(final UUID id, final Type type, final Float latitude, final Float longitude,
                   final String description, final User requester, final Set<Volunteer> volunteers) {
        this.id = id;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.requester = requester;
        this.volunteers = volunteers;
        this.fulfilledDate = null;
        validateState();
    }

    public Request(final Type type, final Status status, final Float latitude, final Float longitude,
                   final String description, final User requester) {
        this.type = type;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.requester = requester;
        this.volunteers = null;
        this.fulfilledDate = null;
        validateState();
    }

    public Request(final Type type, final Float latitude, final Float longitude,
                   final String description, final User requester) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.requester = requester;
        this.volunteers = null;
        this.fulfilledDate = null;
        validateState();
    }

    private void validateState() {
        assertLocationNotNull();
        assertDescriptionNotNull();
        assertTypeNotNull();
        assertDescriptionNotBlank();
        assertDescription();
    }

    private void assertDescriptionNotBlank() {
        if (description.isBlank()) {
            throw new RequestStateException("request.description.notBlank", "Description should not be blanked");
        }
    }

    private void assertLocationNotNull() {
        if (longitude == null || latitude == null) {
            throw new RequestStateException("request.location.notNull", "Location should not be null");
        }
    }

    private void assertDescriptionNotNull() {
        if (description == null) {
            throw new RequestStateException("request.description.notNull", "Description should not be null");
        }
    }

    private void assertTypeNotNull() {
        if (type == null) {
            throw new RequestStateException("request.type.notNull", "Type should not be null");
        }
    }


    private void assertDescription() {
        if (description.length() > 300) {
            throw new RequestStateException("request.description.tooLong", "Description should have 300 character maximum");
        }
    }
}
