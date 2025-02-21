package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.ProductDetail;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile file) throws Exception;

    void saveImageToProductDetail(ProductDetail productDetail, MultipartFile file) throws Exception;

    void updateFeatureImageForProductDetail(ProductDetail productDetail, MultipartFile featureImageFile) throws Exception;

}