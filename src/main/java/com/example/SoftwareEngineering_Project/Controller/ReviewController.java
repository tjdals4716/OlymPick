package com.example.SoftwareEngineering_Project.Controller;

import com.example.SoftwareEngineering_Project.DTO.ReviewDTO;
import com.example.SoftwareEngineering_Project.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 작성
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestPart("reviewData") ReviewDTO reviewDTO,
                                                  @RequestPart(value = "mediaFile", required = false) MultipartFile mediaFile) {
        ReviewDTO createdReview = reviewService.createReview(reviewDTO, mediaFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    //리뷰 좋아요, 동일 유저 좋아요 중복 방지
    @PostMapping("/likes/{userId}/{reviewId}")
    public ResponseEntity<ReviewDTO> toggleReviewLike(@PathVariable Long reviewId, @PathVariable Long userId) {
        ReviewDTO updatedReview = reviewService.toggleReviewLike(reviewId, userId);
        return ResponseEntity.ok(updatedReview);
    }

    //리뷰 조회
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    //모든 리뷰 조회
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    //해당 상품에 대한 리뷰 조회
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    //특정 유저가 작성한 리뷰 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    //리뷰 수정
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    //리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}