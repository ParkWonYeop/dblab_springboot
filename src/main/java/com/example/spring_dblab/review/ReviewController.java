package com.example.spring_dblab.review;

import com.example.spring_dblab.dto.ReviewDeleteDto;
import com.example.spring_dblab.dto.ReviewDto;
import com.example.spring_dblab.entitiy.EventReview;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 이벤트 리뷰 관련 HTTP 요청을 처리하는 컨트롤러.
 * 리뷰 조회, 추가, 삭제 기능을 제공한다.
 */
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 특정 이벤트의 리뷰를 조회합니다.
     *
     * @param eventId 조회할 이벤트의 ID
     * @return 해당 이벤트의 리뷰 목록
     * @throws Exception 리뷰 조회 과정에서 발생할 수 있는 예외
     */
    @GetMapping()
    public List<EventReview> getReview(@RequestParam(name="id") String eventId) throws Exception {
        return reviewService.getReview(eventId);
    }

    /**
     * 새로운 리뷰를 추가합니다.
     *
     * @param reviewDto 추가할 리뷰 정보를 담은 데이터 전송 객체
     * @return 리뷰 추가 성공 여부를 나타내는 문자열
     * @throws Exception 리뷰 추가 과정에서 발생할 수 있는 예외
     */
    @PostMapping
    public String addReview(@RequestBody ReviewDto reviewDto) throws Exception {
        return reviewService.addReview(reviewDto);
    }

    /**
     * 기존 리뷰를 삭제합니다.
     *
     * @param reviewDeleteDto 삭제할 리뷰 정보를 담은 데이터 전송 객체
     * @return 리뷰 삭제 성공 여부를 나타내는 문자열
     * @throws Exception 리뷰 삭제 과정에서 발생할 수 있는 예외
     */
    @DeleteMapping
    public String deleteReview(@RequestBody ReviewDeleteDto reviewDeleteDto) throws Exception {
        return reviewService.deleteReview(reviewDeleteDto);
    }
}
