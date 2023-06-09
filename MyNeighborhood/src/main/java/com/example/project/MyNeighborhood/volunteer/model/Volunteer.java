package com.example.project.MyNeighborhood.volunteer.model;

import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "volunteers")
public class Volunteer {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
}
