package com.example.project.MyNeighborhood.profilePicture.repository;

import com.example.project.MyNeighborhood.profilePicture.model.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
}
