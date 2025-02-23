package org.fpoly.capstone.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.Image;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.repository.ImageRepository;
import org.fpoly.capstone.repository.ProductDetailRepository;
import org.fpoly.capstone.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2  // Lombok annotation for log4j2 logging
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ProductDetailRepository productDetailRepository;
    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) throws Exception {
        try {
            log.info("Uploading image: {}", file.getOriginalFilename());
            Map uploadResult = this.cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto"
                    )
            );
            String imageUrl = (String) uploadResult.get("secure_url");
            log.info("Image uploaded successfully. URL: {}", imageUrl);
            return imageUrl;
        } catch (IOException io) {
            log.error("IOException occurred while uploading image: {}", io.getMessage(), io);
            throw new RuntimeException("IOException occurred while uploading image: " + io.getMessage(), io);
        } catch (Exception e) {
            log.error("An error occurred during image upload: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred during image upload: " + e.getMessage(), e);
        }
    }

    @Override
    public void saveImageToProductDetail(ProductDetail productDetail, MultipartFile file) throws Exception {
        try {
            log.info("Saving image to product detail: {}", file.getOriginalFilename());
            String imageUrl = this.uploadImage(file);

            Image image = new Image();
            image.setName(file.getOriginalFilename());
            image.setUrl(imageUrl);
            image.setProductDetail(productDetail);

            this.imageRepository.save(image);
            log.info("Image saved to product detail. ProductDetail ID: {}", productDetail.getId());
        } catch (IOException io) {
            log.error("Failed to upload image to product detail: {}", io.getMessage(), io);
            throw new RuntimeException("Failed to upload image to product detail: " + io.getMessage(), io);
        } catch (Exception e) {
            log.error("An error occurred while saving image to product detail: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while saving image to product detail: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateFeatureImageForProductDetail(ProductDetail productDetail, MultipartFile featureImageFile) throws Exception {
        try {
            log.info("Updating feature image for product detail. ProductDetail ID: {}", productDetail.getId());
            String featureImageUrl = this.uploadImage(featureImageFile);

            productDetail.setFeatureImage(featureImageUrl);
            this.productDetailRepository.save(productDetail);
            log.info("Feature image updated successfully for ProductDetail ID: {}", productDetail.getId());
        } catch (IOException io) {
            log.error("Failed to upload feature image for product detail: {}", io.getMessage(), io);
            throw new RuntimeException("Failed to upload feature image for product detail: " + io.getMessage(), io);
        } catch (Exception e) {
            log.error("An error occurred while updating feature image for product detail: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while updating feature image for product detail: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> getImagesUrlByProductDetailId(Long productDetailId) {
        return this.imageRepository.findImagesUrlByProductDetailId(productDetailId);
    }

    private boolean isFileNameExist(String fileName) {
        try {
            log.debug("Checking if file name exists in the database: {}", fileName);
            return this.imageRepository.existsByName(fileName);
        } catch (Exception e) {
            log.error("An error occurred while checking file name existence: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while checking file name existence: " + e.getMessage(), e);
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;

        log.debug("Generated new file name: {}", newFileName);

        // Ensure the file name is unique
        while (this.isFileNameExist(newFileName)) {
            newFileName = UUID.randomUUID().toString() + extension;
            log.debug("Generated new file name (retries): {}", newFileName);
        }

        return newFileName;
    }
}
