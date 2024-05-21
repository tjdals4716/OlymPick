package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.DTO.ReviewDTO;
import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.ReviewEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import com.example.SoftwareEngineering_Project.Repository.ProductRepository;
import com.example.SoftwareEngineering_Project.Repository.ReviewRepository;
import com.example.SoftwareEngineering_Project.Repository.UserRepository;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final Storage storage;
//예외수정완료
    //리뷰 작성
    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO, MultipartFile mediaFile) {
        UserEntity user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("유저의 id가 " + reviewDTO.getUserId() + "번인 사용자를 찾을 수 없습니다."));
        ProductEntity product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("제품의 id가" + reviewDTO.getProductId() + "번인 제품을 찾을 수 없습니다."));

        String mediaUrl = null;
        if (mediaFile != null && !mediaFile.isEmpty()) {
            try {
                UUID uuid = UUID.randomUUID();
                String fileExtension = mediaFile.getOriginalFilename().substring(mediaFile.getOriginalFilename().lastIndexOf("."));
                String fileName = uuid.toString() + fileExtension;
                String contentType;
                switch (fileExtension.toLowerCase()) {
                    case ".jpg":
                    case ".jpeg":
                        contentType = "image/jpeg";
                        break;
                    case ".png":
                        contentType = "image/png";
                        break;
                    case ".bmp":
                        contentType = "image/bmp";
                        break;
                    case ".gif":
                        contentType = "image/gif";
                        break;
                    case ".mp4":
                        contentType = "video/mp4";
                        break;
                    case ".avi":
                        contentType = "video/avi";
                        break;
                    case ".wmv":
                        contentType = "video/wmv";
                        break;
                    case ".mpeg:":
                        contentType = "video/mpeg";
                        break;
                    default:
                        contentType = "application/octet-stream";
                }
                BlobId blobId = BlobId.of("olympick", fileName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                        .setContentType(contentType)
                        .setContentDisposition("inline; filename=" + mediaFile.getOriginalFilename())
                        .build();
                storage.create(blobInfo, mediaFile.getBytes());
                mediaUrl = "https://storage.googleapis.com/olympick/" + fileName;
            } catch (IOException e) {
                throw new RuntimeException("미디어 파일 업로드 중 오류가 발생했습니다. 오류 내용 : ", e);
            }
        }

        ReviewEntity reviewEntity = reviewDTO.dtoToEntity(user, product);
        reviewEntity.setMediaUrl(mediaUrl);
        reviewEntity.setStatusDateTime(LocalDateTime.now());
        ReviewEntity savedReview = reviewRepository.save(reviewEntity);
        logger.info("리뷰 작성 완료! " + savedReview);
        return ReviewDTO.entityToDto(savedReview);
    }

    //리뷰 좋아요, 동일 유저 좋아요 중복 방지
    @Override
    public ReviewDTO toggleReviewLike(Long reviewId, Long userId) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰 id가 " + reviewId + "번인 해당 리뷰가 존재하지 않습니다."));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 id가" + userId + "번인 사용자가 존재하지 않습니다."));

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
                .orElseThrow(() -> new RuntimeException("리뷰 id가 " + id + "번인 리뷰를 찾을 수 없습니다."));
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
                .orElseThrow(() -> new RuntimeException("제품의 id가 " + productId + "번인 제품을 찾을 수 없습니다."));
        List<ReviewEntity> reviewEntities = reviewRepository.findByProduct(product);
        return reviewEntities.stream()
                .map(ReviewDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //특정 유저가 작성한 리뷰 조회
    @Override
    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 id가 " + userId + "번인 사용자를 찾을 수 없습니다."));
        List<ReviewEntity> reviewEntities = reviewRepository.findByUser(user);
        return reviewEntities.stream()
                .map(ReviewDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //리뷰 수정
    @Override
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰 id가 " + id + "번인 리뷰를 찾을 수 없습니다."));
        ProductEntity product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("제품의 id가 " + reviewDTO.getProductId() + "번인 제품을 찾을 수 없습니다."));

        reviewEntity.setTitle(reviewDTO.getTitle());
        reviewEntity.setContent(reviewDTO.getContent());
        reviewEntity.setMediaUrl(reviewDTO.getMediaUrl());
        reviewEntity.setLikes(reviewDTO.getLikes());
        reviewEntity.setProduct(product);

        ReviewEntity updatedReview = reviewRepository.save(reviewEntity);
        logger.info("리뷰 id가 " + updatedReview + "번인 리뷰 업데이트 완료!");
        return ReviewDTO.entityToDto(updatedReview);
    }

    //리뷰 삭제
    @Override
    public void deleteReview(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰 id가 " + id + "번인 리뷰를 찾을 수 없습니다."));
        reviewRepository.delete(reviewEntity);
        logger.info("리뷰 id가 " + id + "번인 리뷰 삭제 완료!");
    }
}