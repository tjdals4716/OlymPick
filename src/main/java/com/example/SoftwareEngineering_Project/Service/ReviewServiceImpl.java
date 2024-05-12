package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.DTO.ReviewDTO;
import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.ReviewEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import com.example.SoftwareEngineering_Project.Repository.ProductRepository;
import com.example.SoftwareEngineering_Project.Repository.ReviewRepository;
import com.example.SoftwareEngineering_Project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    //리뷰 작성
    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        UserEntity user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. id: " + reviewDTO.getUserId()));
        ProductEntity product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. id: " + reviewDTO.getProductId()));

        ReviewEntity reviewEntity = reviewDTO.dtoToEntity(user, product);
        reviewEntity.setStatusDateTime(LocalDateTime.now());
        ReviewEntity savedReview = reviewRepository.save(reviewEntity);
        logger.info("리뷰 작성 완료! " + savedReview);
        return ReviewDTO.entityToDto(savedReview);
    }

    //리뷰 좋아요, 한 유저당 좋아요 중복 방지
    @Override
    public ReviewDTO toggleReviewLike(Long reviewId, Long userId) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다. reviewId: " + reviewId));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. userId: " + userId));

        if (reviewEntity.getLikedUsers().contains(userEntity)) {
            reviewEntity.getLikedUsers().remove(userEntity);
            userEntity.getLikedReviews().remove(reviewEntity);
            reviewEntity.setLikes(Math.max(reviewEntity.getLikes() - 1, 0));
        } else {
            reviewEntity.getLikedUsers().add(userEntity);
            userEntity.getLikedReviews().add(reviewEntity);
            reviewEntity.setLikes(reviewEntity.getLikes() + 1);
        }

        ReviewEntity savedReview = reviewRepository.save(reviewEntity);
        userRepository.save(userEntity);

        return ReviewDTO.entityToDto(savedReview);
    }

    //리뷰 조회
    @Override
    public ReviewDTO getReviewById(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다. id: " + id));
        return ReviewDTO.entityToDto(reviewEntity);
    }

    //모든 리뷰 조회
    @Override
    public List<ReviewDTO> getAllReviews() {
        List<ReviewEntity> reviewEntities = reviewRepository.findAll();
        return reviewEntities.stream()
                .map(ReviewDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //해당 상품에 대한 리뷰 조회
    @Override
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. id: " + productId));
        List<ReviewEntity> reviewEntities = reviewRepository.findByProduct(product);
        return reviewEntities.stream()
                .map(ReviewDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //특정 유저가 작성한 리뷰 조회
    @Override
    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. id: " + userId));
        List<ReviewEntity> reviewEntities = reviewRepository.findByUser(user);
        return reviewEntities.stream()
                .map(ReviewDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //리뷰 수정
    @Override
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다. id: " + id));
        ProductEntity product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. id: " + reviewDTO.getProductId()));

        reviewEntity.setTitle(reviewDTO.getTitle());
        reviewEntity.setContent(reviewDTO.getContent());
        reviewEntity.setImage(reviewDTO.getImage());
        reviewEntity.setLikes(reviewDTO.getLikes());
        reviewEntity.setProduct(product);

        ReviewEntity updatedReview = reviewRepository.save(reviewEntity);
        logger.info("리뷰 업데이트 완료! " + updatedReview);
        return ReviewDTO.entityToDto(updatedReview);
    }

    //리뷰 삭제
    @Override
    public void deleteReview(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다. id: " + id));
        reviewRepository.delete(reviewEntity);
        logger.info("리뷰 삭제 완료! id: " + id);
    }
}