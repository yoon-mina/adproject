package com.ms.adproject.repository;
import com.ms.adproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserid(String userid);
}