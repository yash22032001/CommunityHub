package com.communityhub.community.hub_app.repository;

import com.communityhub.community.hub_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
