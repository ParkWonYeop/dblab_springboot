package com.example.spring_dblab.event;

import com.example.spring_dblab.entitiy.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.example.spring_dblab.utils.CheckDate.parseDate;

/**
 * 이벤트 관련 HTTP 요청을 처리하는 컨트롤러.
 */
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    /**
     * 페이지 번호에 따라 이벤트 목록을 페이지 형식으로 반환합니다.
     *
     * @param page 페이지 번호
     * @return 해당 페이지에 해당하는 이벤트 목록
     * @throws Exception 페이지 로딩 중 발생할 수 있는 예외
     */
    @GetMapping("/page")
    public Page<Event> getEventPage(@RequestParam(name="page") String page) throws Exception {
        return eventService.getEventPage(page);
    }

    /**
     * 특정 ID를 가진 이벤트를 반환합니다.
     *
     * @param id 이벤트 ID
     * @return 해당 ID를 가진 이벤트
     * @throws Exception 이벤트 조회 중 발생할 수 있는 예외
     */
    @GetMapping("/id")
    public Event getEvent(@RequestParam(name="id") String id) throws Exception {
        return eventService.getEvent(id);
    }

    /**
     * 시작 날짜와 종료 날짜 사이의 이벤트 목록을 반환합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간에 해당하는 이벤트 목록
     */
    @GetMapping("/date")
    public List<Event> getEventByDate(@RequestParam(name="startDate") String startDate, @RequestParam(name="endDate") String endDate) {
        return eventService.getEventByDate(parseDate(startDate), parseDate(endDate));
    }

    /**
     * 이벤트 이름에 따라 이벤트 목록을 반환합니다.
     *
     * @param name 이벤트 이름
     * @return 해당 이름을 가진 이벤트 목록
     */
    @GetMapping("/name")
    public List<Event> getEventByName(@RequestParam(name="eventName") String name) {
        return eventService.getEventByName(name);
    }
}
