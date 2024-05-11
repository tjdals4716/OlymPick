package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.DTO.ReviewDTO;
import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.ReviewEntity;
import com.example.SoftwareEngineering_Project.Repository.ProductRepository;
import com.example.SoftwareEngineering_Project.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        ProductEntity product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. id: " + reviewDTO.getProductId()));
        ReviewEntity reviewEntity = reviewDTO.dtoToEntity(product);
        ReviewEntity savedReview = reviewRepository.save(reviewEntity);
        logger.info("리뷰 작성 완료! " + savedReview);
        return ReviewDTO.entityToDto(savedReview);
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다. id: " + id));
        return ReviewDTO.entityToDto(reviewEntity);
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        List<ReviewEntity> reviewEntities = reviewRepository.findAll();
        return reviewEntities.stream()
                .map(ReviewDTO::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. id: " + productId));
        List<ReviewEntity> reviewEntities = reviewRepository.findByProduct(product);
        return reviewEntities.stream()
                .map(ReviewDTO::entityToDto)
                .collect(Collectors.toList());
    }

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

    @Override
    public void deleteReview(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다. id: " + id));
        reviewRepository.delete(reviewEntity);
        logger.info("리뷰 삭제 완료! id: " + id);
    }
}