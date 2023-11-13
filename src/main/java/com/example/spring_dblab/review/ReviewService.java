package com.example.spring_dblab.review;

import com.example.spring_dblab.dto.ReviewDeleteDto;
import com.example.spring_dblab.dto.ReviewDto;
import com.example.spring_dblab.entitiy.Event;
import com.example.spring_dblab.entitiy.EventReview;
import com.example.spring_dblab.entitiy.User;
import com.example.spring_dblab.repository.EventRepository;
import com.example.spring_dblab.repository.EventReviewRepository;
import com.example.spring_dblab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.spring_dblab.utils.SecurityUtil.getCurrentMemberEmail;

/**
 * 이벤트 리뷰와 관련된 서비스를 제공하는 클래스.
 * 리뷰 조회, 추가, 삭제 기능을 포함한다.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final EventReviewRepository eventReviewRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    /**
     * 특정 이벤트의 리뷰를 조회합니다.
     *
     * @param eventId 조회할 이벤트의 ID
     * @return 해당 이벤트의 리뷰 목록
     * @throws Exception 리뷰 조회 과정에서 발생할 수 있는 예외
     */
    public List<EventReview> getReview(String eventId) throws Exception {
        try {
            Optional<Event> event = eventRepository.findEventById(Long.parseLong(eventId));

            if(event.isPresent()) {
                log.info("getReview : Success");
                return eventReviewRepository.findByEvent(event.get());
            }

            log.error("getReview : Not Found Event");
            throw new Exception("Not Found Event");
        } catch(Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 새로운 리뷰를 추가합니다.
     *
     * @param reviewDto 추가할 리뷰 정보를 담은 데이터 전송 객체
     * @return 리뷰 추가 성공 여부를 나타내는 문자열
     * @throws Exception 리뷰 추가 과정에서 발생할 수 있는 예외
     */
    public String addReview(ReviewDto reviewDto) throws Exception {
        try {
            String userEmail = getCurrentMemberEmail();
            Optional<User> user = userRepository.findByEmail(userEmail);

            if(user.isPresent()) {
                Optional<Event> event = eventRepository.findById(reviewDto.getEventId());

                if(event.isPresent()) {
                    EventReview eventReview = new EventReview(user.get(), event.get(), reviewDto.getReview(), reviewDto.getScore());
                    eventReviewRepository.save(eventReview);

                    log.info("addReview : Success");
                    return "success";
                }

                log.error("addReview : Not Found Event");
                throw new Exception("Not Found Event");
            }

            log.error("addReview : Not Found User");
            throw new Exception("Not Found User");
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }

    /**
     * 기존 리뷰를 삭제합니다.
     *
     * @param reviewDeleteDto 삭제할 리뷰 정보를 담은 데이터 전송 객체
     * @return 리뷰 삭제 성공 여부를 나타내는 문자열
     * @throws Exception 리뷰 삭제 과정에서 발생할 수 있는 예외
     */
    public String deleteReview(ReviewDeleteDto reviewDeleteDto) throws Exception {
        try {
            String userEmail = getCurrentMemberEmail();
            Optional<User> user = userRepository.findByEmail(userEmail);

            if(user.isPresent()) {
                    Optional<EventReview> eventReview = eventReviewRepository.findEventReviewById(reviewDeleteDto.getReviewId());
                    if(eventReview.isPresent()) {
                        if(Objects.equals(eventReview.get().getUser().getEmail(), user.get().getEmail())) {
                            eventReviewRepository.delete(eventReview.get());
                            log.info("deleteReview : Success");
                            return "success";
                        }
                    }
                    log.error("deleteReview : Not Found Review");
                    throw new Exception("Not Found Review");
                }
            log.error("deleteReview : Not Found User");
            throw new Exception("Not Found User");
        } catch (Exception err) {
            log.error(String.valueOf(err));
            throw err;
        }
    }
}
