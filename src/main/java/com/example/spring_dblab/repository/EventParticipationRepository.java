package com.example.spring_dblab.repository;

import com.example.spring_dblab.entitiy.Event;
import com.example.spring_dblab.entitiy.EventParticipation;
import com.example.spring_dblab.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
    Optional<EventParticipation> findEventParticipationByEventAndUser(Event event, User user);
    List<EventParticipation> findEventParticipationsByEvent(Event event);
}
