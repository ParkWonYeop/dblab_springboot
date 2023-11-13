package com.example.spring_dblab.repository;

import com.example.spring_dblab.entitiy.Event;
import com.example.spring_dblab.entitiy.EventReview;
import com.example.spring_dblab.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventReviewRepository extends JpaRepository<EventReview,Long> {
    List<EventReview> findByEvent(Event event);
    Optional<EventReview> findEventReviewByEventAndUser(Event event, User user);
    Optional<EventReview> findEventReviewById(Long id);
}
