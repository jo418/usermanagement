package com.example.users.repositories;

import com.example.users.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndUserRepository extends JpaRepository<EndUser, Long> {
}
