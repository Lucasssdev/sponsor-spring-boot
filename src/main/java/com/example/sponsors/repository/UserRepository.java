package com.example.sponsors.repository;

import com.example.sponsors.model.Sponsors;
import com.example.sponsors.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
