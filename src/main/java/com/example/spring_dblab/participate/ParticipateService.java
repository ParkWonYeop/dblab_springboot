package com.example.spring_dblab.participate;

import com.example.spring_dblab.dto.AlarmDto;
import com.example.spring_dblab.dto.ParticipateDto;
import com.example.spring_dblab.entitiy.Event;
import com.example.spring_dblab.entitiy.EventParticipation;
import com.example.spring_dblab.entitiy.User;
import com.example.spring_dblab.entitiy.UserAlarm;
import com.example.spring_dblab.repository.EventParticipationRepository;
import com.example.spring_dblab.repository.EventRepository;
import com.example.spring_dblab.repository.UserAlarmRepository;
import com.example.spring_dblab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.spring_dblab.utils.SecurityUtil.getCurrentMemberEmail;

/**
 * 사용자 알림 설정 및 이벤트 참여와 관련된 서비스를 제공하는 클래스.
 * 알림 단어 설정 및 삭제, 이벤트 참여 및 참여 취소 기능을 포함한다.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipateService {
    private final UserAlarmRepository userAlarmRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventParticipationRepository eventParticipationRepository;

    /**
     * 사용자 알림 단어를 설정합니다.
     *
     * @param alarmDto 알림 단어 정보를 담은 데이터 전송 객체
     * @return 알림 단어 설정 성공 여부를 나타내는 문자열
     * @throws Exception 알림 단어 설정 과정에서 발생할 수 있는 예외
     */
    public String setAlarmWord(AlarmDto alarmDto) throws Exception {
        try {
            String word = alarmDto.getWord();
            String userEmail = getCurrentMemberEmail();
            Optional<User> user = userRepository.findByEmail(userEmail);
            if(user.isPresent()) {
                UserAlarm userAlarm = new UserAlarm(user.get(), word);
                userAlarmRepository.save(userAlarm);
                log.info("setAlarmWord : Success");
                return "success";
            }
            log.error("setAlarmWord : Not Found User");
            throw new Exception("Not Found User");
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 설정된 사용자 알림 단어를 삭제합니다.
     *
     * @param alarmDto 삭제할 알림 단어 정보를 담은 데이터 전송 객체
     * @return 알림 단어 삭제 성공 여부를 나타내는 문자열
     * @throws Exception 알림 단어 삭제 과정에서 발생할 수 있는 예외
     */
    public String deleteAlarmWord(AlarmDto alarmDto) throws Exception {
        try {
            String word = alarmDto.getWord();
            String userEmail = getCurrentMemberEmail();
            Optional<User> user = userRepository.findByEmail(userEmail);
            if(user.isPresent()) {
                Optional<UserAlarm> userAlarm = userAlarmRepository.findUserAlarmByUserAndWord(user.get(),word);
                if(userAlarm.isPresent()) {
                    userAlarmRepository.delete(userAlarm.get());
                    log.info("deleteAlarmWord : Success");
                    return "success";
                }
                log.error("setAlarmWord : Not Found AlarmWord");
                throw new Exception("Not Found AlarmWord");
            }

            log.error("deleteAlarmWord : Not Found User");
            throw new Exception("Not Found User");
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 이벤트에 참여합니다.
     *
     * @param participateDto 이벤트 참여 정보를 담은 데이터 전송 객체
     * @return 이벤트 참여 성공 여부를 나타내는 문자열
     * @throws Exception 이벤트 참여 과정에서 발생할 수 있는 예외
     */
    public String participateEvent(ParticipateDto participateDto) throws Exception {
        try {
            Optional<User> user = userRepository.findByEmail(getCurrentMemberEmail());

            if(user.isPresent()) {
                Optional<Event> event = eventRepository.findById(participateDto.getEventId());

                if(event.isPresent()) {
                    Optional<EventParticipation> checkParticipation = eventParticipationRepository.findEventParticipationByEventAndUser(event.get(),user.get());

                    if(checkParticipation.isPresent()) {
                        log.error("participateEvent : already participate");
                        throw new Exception("already participate");
                    }

                    if(event.get().getMaxParticipation() == event.get().getCurrentParticipation()) {
                        log.error("participateEvent : MaxParticipate");
                        throw new Exception("Full participation");
                    }

                    event.get().setCurrentParticipation(event.get().getCurrentParticipation()+1);
                    eventRepository.save(event.get());
                    EventParticipation eventParticipation = new EventParticipation(user.get(),event.get());
                    eventParticipationRepository.save(eventParticipation);

                    log.info("participateEvent : Success");
                    return "success";
                }

                log.error("participateEvent : Not Found Event");
                throw new Exception("Not Found Event");
            }

            log.error("participateEvent : Not Found User");
            throw new Exception("Not Found User");
        } catch(Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 이벤트 참여를 취소합니다.
     *
     * @param participateDto 참여 취소할 이벤트 정보를 담은 데이터 전송 객체
     * @return 이벤트 참여 취소 성공 여부를 나타내는 문자열
     * @throws Exception 이벤트 참여 취소 과정에서 발생할 수 있는 예외
     */
    public String deleteParticipateEvent(ParticipateDto participateDto) throws Exception {
        try {
            Optional<User> user = userRepository.findByEmail(getCurrentMemberEmail());

            if(user.isPresent()) {
                Optional<Event> event = eventRepository.findById(participateDto.getEventId());

                if(event.isPresent()) {
                    Optional<EventParticipation> eventParticipation = eventParticipationRepository.findEventParticipationByEventAndUser(event.get(),user.get());

                    if(eventParticipation.isPresent()) {
                        eventParticipationRepository.delete(eventParticipation.get());
                        event.get().setCurrentParticipation(event.get().getCurrentParticipation()-1);
                        eventRepository.save(event.get());
                        log.info("deleteParticipateEvent : Success");
                        return "success";
                    }
                    log.error("deleteParticipateEvent : Not Found Participate Event");
                    throw new Exception("Not Found Participate Event");
                }

                log.error("deleteParticipateEvent : Not Found Event");
                throw new Exception("Not Found Event");
            }

            log.error("deleteParticipateEvent : Not Found User");
            throw new Exception("Not Found User");
        } catch(Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }
}
