package com.datingbot.repository;

import com.datingbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findTopByOrderByIdDesc();

    User findTopByOrderByIdAsc();

    @Query(value = "SELECT nextval('serial_seq')", nativeQuery = true)
    Long getNextSeriesId();

    @Query(value = "SELECT setval('serial_seq', :isId)", nativeQuery = true)
    void resetSeries(@Param("isId") long id);

    User findByUserId(long userId);
}
