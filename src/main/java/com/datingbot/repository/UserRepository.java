package com.datingbot.repository;

import com.datingbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findTopByOrderByIdAsc();

    List<User> findAllBySex(int oppositeSex);

    User findByUserId(long userId);

    boolean existsByUserId(long userId);

    List<User> findAllByUserId(long userId);
}
