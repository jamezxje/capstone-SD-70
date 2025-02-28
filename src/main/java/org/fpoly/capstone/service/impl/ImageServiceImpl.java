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
@Log4j2
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

    @Override
    public void deleteImage(String imageUrl) {
        try {
            // Extract the public_id from the image URL (you need to store public_id when uploading images)
            String publicId = this.extractPublicIdFromUrl(imageUrl);
            log.info("Extracted Public ID: {}", publicId);

            if (publicId != null) {
                log.info("Deleting image from Cloudinary. Public ID: {}", publicId);

                // Delete image from Cloudinary using the public ID
                Map<String, Object> result = this.cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

                // Check the result for successful deletion
                if ("ok".equals(result.get("result"))) {
                    log.info("Image deleted successfully from Cloudinary. Public ID: {}", publicId);
                } else {
                    log.error("Failed to delete image from Cloudinary. Public ID: {}", publicId);
                }
            } else {
                log.warn("No public ID found for image URL: {}", imageUrl);
            }
        } catch (Exception e) {
            log.error("An error occurred while deleting image from Cloudinary: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while deleting image from Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteImageByProductDetailId(Long productDetailId) {
        try {
            // Retrieve the ProductDetail entity
            ProductDetail productDetail = this.productDetailRepository.findById(productDetailId)
                    .orElseThrow(() -> new RuntimeException("ProductDetail not found with ID: " + productDetailId));

            // Loop through each image associated with the product detail and delete it
            for (Image image : productDetail.getImages()) {
                // Deleting image from Cloudinary
                this.deleteImage(image.getUrl());
            }

            // After deleting the images from Cloudinary, remove the references from ProductDetail
//            productDetail.setImages(List.of()); // Clear the image list
//            this.productDetailRepository.save(productDetail); // Save the ProductDetail with an empty image list

            log.info("Successfully deleted all images for ProductDetail ID: {}", productDetailId);
        } catch (Exception e) {
            log.error("An error occurred while deleting images for ProductDetail ID: {}", productDetailId, e);
            throw new RuntimeException("An error occurred while deleting images for ProductDetail ID: " + productDetailId, e);
        }
    }


    private String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl != null && imageUrl.contains("/image/upload/")) {
            // Extract part after /image/upload/
            String[] parts = imageUrl.split("/image/upload/");
            if (parts.length > 1) {
                // After /image/upload/, the publicId is typically the next part before the version info (v1740468688)
                String publicIdWithExtension = parts[1].split("\\?")[0]; // Split to remove query parameters

                // The publicId is the part after the version (v1740468688), so we split by "/"
                String[] publicIdParts = publicIdWithExtension.split("/");
                String publicId = publicIdParts[publicIdParts.length - 1].split("\\.")[0]; // This gives the actual public ID

                return publicId;
            }
        }
        log.error("Invalid URL format or no /image/upload/ path found. URL: {}", imageUrl); // Log invalid URL case
        return null;
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
