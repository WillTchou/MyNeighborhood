package com.example.project.MyNeighborhood.governmentIdentity.model;

import com.example.project.MyNeighborhood.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "governmentIdentity")
public class GovernmentIdentity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    private String name;
    private String type;
    @Lob
    @Column(name = "data", length = 10000)
    private byte[] data;
}
