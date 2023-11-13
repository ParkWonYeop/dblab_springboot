package com.example.spring_dblab.repository;

import com.example.spring_dblab.entitiy.User;
import com.example.spring_dblab.entitiy.UserAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAlarmRepository extends JpaRepository<UserAlarm,Long> {
    Optional<UserAlarm> findUserAlarmByUserAndWord(User user, String word);
}
