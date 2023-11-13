package com.example.spring_dblab.organize;

import com.example.spring_dblab.dto.EventDeleteDto;
import com.example.spring_dblab.dto.EventDto;
import com.example.spring_dblab.dto.EventEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 이벤트 관리와 관련된 HTTP 요청을 처리하는 컨트롤러.
 * 이벤트 추가, 수정, 삭제 기능을 제공한다.
 */
@RestController
@RequestMapping("/organize")
@RequiredArgsConstructor
public class OrganizeController {
    private final OrganizeService organizeService;

    @GetMapping()
    public String checkOrganizer() {
        return organizeService.checkOrganizer();
    }

    /**
     * 새로운 이벤트를 추가합니다.
     *
     * @param eventDto 이벤트 정보를 담은 데이터 전송 객체
     * @return 이벤트 추가 결과를 나타내는 문자열
     * @throws Exception 이벤트 추가 과정에서 발생할 수 있는 예외
     */
    @PostMapping()
    public String addEvent(@RequestBody EventDto eventDto) throws Exception {
        return organizeService.addEvent(eventDto);
    }

    /**
     * 기존 이벤트를 수정합니다.
     *
     * @param eventEditDto 수정할 이벤트 정보를 담은 데이터 전송 객체
     * @return 이벤트 수정 결과를 나타내는 문자열
     * @throws Exception 이벤트 수정 과정에서 발생할 수 있는 예외
     */
    @PutMapping()
    public String updateEvent(@RequestBody EventEditDto eventEditDto) throws Exception {
        return organizeService.updateEvent(eventEditDto);
    }

    /**
     * 기존 이벤트를 삭제합니다.
     *
     * @param eventDeleteDto 삭제할 이벤트 정보를 담은 데이터 전송 객체
     * @return 이벤트 삭제 결과를 나타내는 문자열
     * @throws Exception 이벤트 삭제 과정에서 발생할 수 있는 예외
     */
    @DeleteMapping()
    public String deleteEvent(@RequestBody EventDeleteDto eventDeleteDto) throws Exception {
        return organizeService.deleteEvent(eventDeleteDto);
    }
}
