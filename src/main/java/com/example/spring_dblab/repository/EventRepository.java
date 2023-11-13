package com.example.spring_dblab.repository;

import com.example.spring_dblab.entitiy.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    Page<Event> findAll(Pageable pageable);
    List<Event> findByName(String name);
    List<Event> findEventsByCreatedAtBetween(LocalDateTime createdAt, LocalDateTime createdAt2);
    Optional<Event> findEventById(Long Id);
}
