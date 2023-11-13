package com.example.spring_dblab.organize;

import com.example.spring_dblab.dto.EventDeleteDto;
import com.example.spring_dblab.dto.EventDto;
import com.example.spring_dblab.dto.EventEditDto;
import com.example.spring_dblab.entitiy.Event;
import com.example.spring_dblab.entitiy.EventParticipation;
import com.example.spring_dblab.entitiy.User;
import com.example.spring_dblab.entitiy.UserAlarm;
import com.example.spring_dblab.repository.EventParticipationRepository;
import com.example.spring_dblab.repository.EventRepository;
import com.example.spring_dblab.repository.UserAlarmRepository;
import com.example.spring_dblab.repository.UserRepository;
import com.example.spring_dblab.utils.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.spring_dblab.utils.SecurityUtil.getCurrentMemberEmail;

/**
 * 이벤트 관리와 관련된 서비스를 제공하는 클래스.
 * 이벤트 추가, 수정, 삭제 기능을 제공하며, 사용자 이메일에 따른 이벤트 관리 및 사용자 알림 기능을 포함한다.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizeService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserAlarmRepository userAlarmRepository;
    private final EmailService emailService;
    private final EventParticipationRepository eventParticipationRepository;

    public String checkOrganizer() {
        return "success";
    }

    /**
     * 새로운 이벤트를 추가합니다.
     *
     * @param eventDto 추가할 이벤트 정보를 담은 데이터 전송 객체
     * @return 이벤트 추가 성공 여부를 나타내는 문자열
     * @throws Exception 이벤트 추가 과정에서 발생할 수 있는 예외
     */
    public String addEvent(EventDto eventDto) throws Exception {
        try {
            String name = eventDto.getName();
            String description = eventDto.getDescription();
            String userEmail = getCurrentMemberEmail();
            Optional<User> user = userRepository.findByEmail(userEmail);

            if(user.isPresent()) {
                Event event = new Event(name, description, user.get(), eventDto.getMaxParticipation());
                eventRepository.save(event);
            } else {
                log.error("addEvent : User not Found");
                throw new Exception("User not Found");
            }

            List<UserAlarm> userAlarmList = userAlarmRepository.findAll();

            for(UserAlarm i : userAlarmList) {
                if(description.contains(i.getWord())) {
                    User alaramUser = i.getUser();
                    this.emailService.sendMail(alaramUser.getEmail(), i.getWord());
                }
            }

            log.info("addEvent : Success");
            return "success";
        } catch(Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 기존 이벤트를 수정합니다.
     *
     * @param eventEditDto 수정할 이벤트 정보를 담은 데이터 전송 객체
     * @return 이벤트 수정 성공 여부를 나타내는 문자열
     * @throws Exception 이벤트 수정 과정에서 발생할 수 있는 예외
     */
    public String updateEvent(EventEditDto eventEditDto) throws Exception {
        try {
            long eventCode = eventEditDto.getEventCode();

            Optional<Event> originalEvent = eventRepository.findById(eventCode);

            if(originalEvent.isPresent()) {
                Event event = originalEvent.get();
                String name = eventEditDto.getName();
                String description = eventEditDto.getDescription();
                String userEmail = getCurrentMemberEmail();
                Optional<User> user = userRepository.findByEmail(userEmail);

                if(user.isPresent()&&user.get()==event.getUser()) {
                    event.setName(name);
                    event.setDescription(description);
                    eventRepository.save(event);
                }

                log.info("updateEvent : Success");
                return "success";
            }

            log.error("updateEvent : Failed");
            throw new Exception("Failed update event");
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 기존 이벤트를 삭제합니다.
     *
     * @param eventDeleteDto 삭제할 이벤트 정보를 담은 데이터 전송 객체
     * @return 이벤트 삭제 성공 여부를 나타내는 문자열
     * @throws Exception 이벤트 삭제 과정에서 발생할 수 있는 예외
     */
    public String deleteEvent(EventDeleteDto eventDeleteDto) throws Exception {
        try {
            long eventCode = eventDeleteDto.getEventId();

            Optional<Event> originalEvent = eventRepository.findById(eventCode);

            if(originalEvent.isPresent()) {
                Event event = originalEvent.get();
                String userEmail = getCurrentMemberEmail();
                log.info(userEmail);
                Optional<User> user = userRepository.findByEmail(userEmail);

                if(user.isPresent()&& Objects.equals(user.get().getEmail(), event.getUser().getEmail())){
                    List<EventParticipation> eventParticipationsList = eventParticipationRepository.findEventParticipationsByEvent(event);
                    eventParticipationRepository.deleteAll(eventParticipationsList);
                    eventRepository.delete(event);
                    log.info("deleteEvent : Success");
                    return "success";
                }

                log.error("deleteEvent : User is not correct");
                throw new Exception("User is not correct");
            }

            log.error("deleteEvent : Failed");
            throw new Exception("Failed delete event");
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }
}
