package com.example.spring_dblab.event;

import com.example.spring_dblab.entitiy.Event;
import com.example.spring_dblab.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 이벤트 관련 서비스를 제공하는 클래스.
 * 특정 페이지의 이벤트 조회, ID 별 이벤트 조회, 날짜 범위에 따른 이벤트 조회, 이름 별 이벤트 조회 등의 기능을 포함한다.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    /**
     * 주어진 페이지 번호에 해당하는 이벤트 페이지를 반환합니다.
     *
     * @param page 페이지 번호
     * @return 해당 페이지에 해당하는 이벤트 페이지
     * @throws Exception 페이지 번호가 유효하지 않거나 조회 중 오류가 발생한 경우
     */
    public Page<Event> getEventPage(String page) throws Exception {
        try {
            int pageInt = Integer.parseInt(page);
            if(pageInt > 0) {
                log.info("getEventPage : Success");
                return eventRepository.findAll(PageRequest.of(pageInt-1,10));
            }
            log.error("getEventPage : Not Found Page");
            throw new Exception("Not Found Page");
        } catch(Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 주어진 ID를 가진 이벤트를 조회합니다.
     *
     * @param id 이벤트 ID
     * @return 해당 ID를 가진 이벤트
     * @throws Exception ID에 해당하는 이벤트가 없거나 조회 중 오류가 발생한 경우
     */
    public Event getEvent(String id) throws Exception {
        try {
            Long eventId = Long.parseLong(id);
            Optional<Event> event = eventRepository.findById(eventId);
            if(event.isPresent()) {
                log.info("getEvent : Success");
                return event.get();
            }
            log.error("getEvent : Event Not Found");
            throw new Exception("Event Not Found");
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 주어진 날짜 범위에 해당하는 이벤트 목록을 조회합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 날짜 범위에 해당하는 이벤트 목록
     */
    public List<Event> getEventByDate(LocalDate startDate, LocalDate endDate) {
        try {
            List<Event> event = eventRepository.findEventsByCreatedAtBetween(startDate.atStartOfDay(),endDate.atStartOfDay());
            log.info("getEventByDate : Success");
            return event;
        } catch(Exception err){
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 주어진 이름을 가진 이벤트 목록을 조회합니다.
     *
     * @param name 이벤트 이름
     * @return 해당 이름을 가진 이벤트 목록
     */
    public List<Event> getEventByName(String name) {
        try {
            List<Event> event = eventRepository.findByName(name);
            log.info("getEventByName : Success");
            return event;
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }
}
