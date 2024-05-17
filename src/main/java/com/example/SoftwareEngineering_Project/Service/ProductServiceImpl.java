package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.DTO.DeliveryDTO;
import com.example.SoftwareEngineering_Project.Entity.BasketEntity;
import com.example.SoftwareEngineering_Project.Entity.DeliveryEntity;
import com.example.SoftwareEngineering_Project.Enum.BasketStatus;
import com.example.SoftwareEngineering_Project.Enum.DeliveryStatus;
import com.example.SoftwareEngineering_Project.Repository.BasketRepository;
import com.example.SoftwareEngineering_Project.DTO.BasketDTO;
import com.example.SoftwareEngineering_Project.DTO.ProductDTO;
import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import com.example.SoftwareEngineering_Project.Repository.DeliveryRepository;
import com.example.SoftwareEngineering_Project.Repository.ProductRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final BasketRepository basketRepository;
    private final DeliveryRepository deliveryRepository;
    private final Storage storage;

    //상품 등록
    @Override
    public ProductDTO createProduct(ProductDTO productDTO, MultipartFile mediaFile) {
        UserEntity userEntity = userRepository.findById(productDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + productDTO.getUserId()));
        String mediaUrl = null;
        if (mediaFile != null && !mediaFile.isEmpty()) {
            try {
                //UUID를 사용함으로써 버킷에 저장되는 미디어 파일들이 파일 이름 중복으로 충돌이 일어나지 않음
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
                throw new RuntimeException("미디어 파일 업로드 중 오류가 발생했습니다.", e);
            }
        }
        ProductEntity productEntity = productDTO.dtoToEntity(userEntity, productDTO.getCategory());
        productEntity.setMediaUrl(mediaUrl);
        ProductEntity savedProduct = productRepository.save(productEntity);
        logger.info("상품 등록 완료! " + savedProduct);
        return ProductDTO.entityToDto(savedProduct);
    }

    //상품 장바구니에 담기, 동일한 상품일 경우 개수만 증가
    @Override
    public BasketDTO addToBasket(Long userId, Long productId, Long quantity) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + userId));

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. productId: " + productId));

        BasketEntity existingBasketItem = basketRepository.findByUser_IdAndProduct_Id(userId, productId);

        if (existingBasketItem != null) {
            Long newQuantity = existingBasketItem.getCount() + quantity;
            existingBasketItem.setCount(newQuantity);
            BasketEntity savedBasket = basketRepository.save(existingBasketItem);

            logger.info("장바구니에 상품의 개수가 증가하였습니다 " + savedBasket);
            return BasketDTO.entityToDto(savedBasket);
        } else {
            BasketDTO basketDTO = new BasketDTO();
            basketDTO.setCount(quantity);
            basketDTO.setUserId(userId);
            basketDTO.setProductId(productId);
            basketDTO.setBasketStatus(BasketStatus.배송준비중); // BasketStatus 설정
            BasketEntity basketEntity = basketDTO.dtoToEntity(userEntity, BasketStatus.배송준비중, productEntity);
            BasketEntity savedBasket = basketRepository.save(basketEntity);

            logger.info("장바구니에 상품 추가 완료! " + savedBasket);
            return BasketDTO.entityToDto(savedBasket);
        }
    }

    //상품 장바구니에서 빼기, 동일한 상품일 경우 개수만 감소, 1에서 뺄 경우 장바구니에서 상품 삭제
    @Override
    public BasketDTO removeFromBasket(Long userId, Long productId) {
        BasketEntity existingBasketItem = basketRepository.findByUser_IdAndProduct_Id(userId, productId);

        if (existingBasketItem != null) {
            if (existingBasketItem.getCount() > 1L) {
                Long newQuantity = existingBasketItem.getCount() - 1L;
                existingBasketItem.setCount(newQuantity);

                // BasketStatus 업데이트
                if (existingBasketItem.getBasketStatus() == BasketStatus.구매완료) {
                    existingBasketItem.setBasketStatus(BasketStatus.배송준비중);
                }

                BasketEntity savedBasket = basketRepository.save(existingBasketItem);

                logger.info("장바구니에 상품의 개수가 감소하였습니다 " + savedBasket);
                return BasketDTO.entityToDto(savedBasket);
            } else {
                // BasketStatus 업데이트
                if (existingBasketItem.getBasketStatus() == BasketStatus.구매완료) {
                    existingBasketItem.setBasketStatus(BasketStatus.배송준비중);
                    basketRepository.save(existingBasketItem);
                }

                basketRepository.delete(existingBasketItem);
                logger.info("장바구니에서 상품 삭제 완료! " + existingBasketItem);
                return null;
            }
        } else {
            throw new RuntimeException("장바구니에 해당 상품이 없습니다. userId: " + userId + ", productId: " + productId);
        }
    }

    //사용자 장바구니에 있는 상품 배송
    @Override
    public List<DeliveryDTO> createDeliveryForBasket(Long userId, DeliveryStatus status) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + userId));

        List<BasketEntity> baskets = basketRepository.findByUser_Id(userId);

        List<DeliveryEntity> deliveryEntities = new ArrayList<>();

        for (BasketEntity basket : baskets) {
            if (basket.getBasketStatus() != BasketStatus.배송준비중) {
                throw new RuntimeException("상품을 구매하셔서 배송중이거나 배송완료된 상태입니다");
            }

            ProductEntity product = basket.getProduct();
            Long basketQuantity = basket.getCount();

            //주문 수량이 재고량 한계치에 맞는지 확인
            if (product.getQuantity() < basketQuantity) {
                throw new RuntimeException(
                        "\n" + product.getName() + " 상품의 재고가 부족하여 배송이 불가능합니다"
                      + "\n" + user.getNickname() + "님이 현재 선택하신 상품 남은 재고 수량 : " + product.getQuantity()
                      + "\n" + user.getNickname() + "님이 현재 선택하신 상품 주문 수량 : " + basketQuantity);
            }

            DeliveryDTO deliveryDTO = new DeliveryDTO();
            deliveryDTO.setUserId(userId);
            deliveryDTO.setBasketId(basket.getId());
            deliveryDTO.setStatus(status);
            deliveryDTO.setStatusDateTime(LocalDateTime.now());
            deliveryDTO.setCount(basketQuantity);

            DeliveryEntity deliveryEntity = deliveryDTO.dtoToEntity(user, basket, status);
            deliveryEntities.add(deliveryEntity);

            // 상품 재고 수량 감소
            Long remainingQuantity = product.getQuantity() - basketQuantity;
            product.setQuantity(remainingQuantity);
            productRepository.save(product);

            // BasketStatus 업데이트
            basket.setBasketStatus(BasketStatus.구매완료);
            basketRepository.save(basket);
        }

        List<DeliveryEntity> savedDeliveries = deliveryRepository.saveAll(deliveryEntities);

        logger.info("장바구니에 담긴 모든 상품의 배송이 시작되었습니다.");
        return savedDeliveries.stream()
                .map(DeliveryDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //배송 상태 수정
    @Override
    public DeliveryDTO updateDeliveryStatus(Long deliveryId, DeliveryStatus status) {
        DeliveryEntity deliveryEntity = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("배송을 찾을 수 없습니다. deliveryId: " + deliveryId));

        BasketEntity basketEntity = deliveryEntity.getBasket();

        deliveryEntity.setStatus(status);
        deliveryEntity.setStatusDateTime(LocalDateTime.now());

        if (status == DeliveryStatus.배송완료) {
            basketEntity.setBasketStatus(BasketStatus.배송완료);
        }

        if(status == DeliveryStatus.배송취소){
            basketEntity.setBasketStatus(BasketStatus.상품교환);
        }

        DeliveryEntity updatedDelivery = deliveryRepository.save(deliveryEntity);
        basketRepository.save(basketEntity);

        logger.info("상품의 배송상태가 변경되었습니다!");
        return DeliveryDTO.entityToDto(updatedDelivery);
    }

    //모든 상품 조회
    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        return products.stream()
                .map(ProductDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //해당 상품만 조회
    @Override
    public ProductDTO getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id: " + id));
        return ProductDTO.entityToDto(productEntity);
    }

    //상품 수정
    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        UserEntity userEntity = userRepository.findById(productDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + productDTO.getUserId()));

        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id: " + id));

        Long newQuantity = productDTO.getQuantity();

        existingProduct.setName(productDTO.getName());
        existingProduct.setContent(productDTO.getContent());
        existingProduct.setQuantity(newQuantity);
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setMediaUrl(productDTO.getMediaUrl());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setUser(userEntity);

        ProductEntity updatedProduct = productRepository.save(existingProduct);
        logger.info("상품 정보 업데이트 완료! " + updatedProduct);
        return ProductDTO.entityToDto(updatedProduct);
    }

    //상품 삭제
    @Override
    public void deleteProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id: " + id));

        productRepository.delete(productEntity);
        logger.info("상품 삭제 완료! id: " + id);
    }

    //카테고리별 상품 조회
    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        List<ProductEntity> products = productRepository.findByCategory(category);
        return products.stream()
                .map(ProductDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //상품 검색
    @Override
    public List<ProductDTO> searchProductsByName(String name) {
        List<ProductEntity> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(ProductDTO::entityToDto)
                .collect(Collectors.toList());
    }
}