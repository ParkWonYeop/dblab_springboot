package com.example.spring_dblab.participate;

import com.example.spring_dblab.dto.AlarmDto;
import com.example.spring_dblab.dto.ParticipateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 이벤트 참여 및 알림 설정과 관련된 HTTP 요청을 처리하는 컨트롤러.
 * 알림 단어 설정 및 삭제, 이벤트 참여 및 참여 취소 기능을 제공한다.
 */
@RestController
@RequestMapping("/participate")
@RequiredArgsConstructor
public class ParticipateController {
    private final ParticipateService participateService;

    /**
     * 사용자 알림 단어를 설정합니다.
     *
     * @param alarmDto 알림 단어 정보를 담은 데이터 전송 객체
     * @return 알림 단어 설정 성공 여부를 나타내는 문자열
     * @throws Exception 알림 단어 설정 과정에서 발생할 수 있는 예외
     */
    @PostMapping("Alarm")
    public String setAlarmWord(@RequestBody AlarmDto alarmDto) throws Exception {
        return participateService.setAlarmWord(alarmDto);
    }

    /**
     * 설정된 사용자 알림 단어를 삭제합니다.
     *
     * @param alarmDto 삭제할 알림 단어 정보를 담은 데이터 전송 객체
     * @return 알림 단어 삭제 성공 여부를 나타내는 문자열
     * @throws Exception 알림 단어 삭제 과정에서 발생할 수 있는 예외
     */
    @DeleteMapping("Alarm")
    public String deleteAlarmWord(@RequestBody AlarmDto alarmDto) throws Exception {
        return participateService.deleteAlarmWord(alarmDto);
    }

    /**
     * 이벤트에 참여합니다.
     *
     * @param participateDto 이벤트 참여 정보를 담은 데이터 전송 객체
     * @return 이벤트 참여 성공 여부를 나타내는 문자열
     * @throws Exception 이벤트 참여 과정에서 발생할 수 있는 예외
     */
    @PostMapping()
    public String participateEvent(@RequestBody ParticipateDto participateDto) throws Exception {
        return participateService.participateEvent(participateDto);
    }

    /**
     * 이벤트 참여를 취소합니다.
     *
     * @param participateDto 참여 취소할 이벤트 정보를 담은 데이터 전송 객체
     * @return 이벤트 참여 취소 성공 여부를 나타내는 문자열
     * @throws Exception 이벤트 참여 취소 과정에서 발생할 수 있는 예외
     */
    @DeleteMapping()
    public String deleteParticipateEvent(@RequestBody ParticipateDto participateDto) throws Exception {
        return participateService.deleteParticipateEvent(participateDto);
    }
}
