package org.fpoly.capstone.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class FrontProductDetailSpecification {

    public static Specification<ProductDetail> filterByRequest(ProductDetailFilterRequest request) {

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.getCategoryId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("product").get("category").get("id"), request.getCategoryId()));
            }

            if (request.getBrandId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("brand").get("id"), request.getBrandId()));
            }

            if (request.getColorId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("color").get("id"), request.getColorId()));
            }

            if (request.getMaterialId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("material").get("id"), request.getMaterialId()));
            }

            if (request.getSizeId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("size").get("id"), request.getSizeId()));
            }

            if (request.getGender() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("gender"), request.getGender()));
            }

            if (request.getStatus() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), ProductVariantStatus.DANG_SU_DUNG));
            }

            // Lọc theo giá
            if (request.getMinPrice() != null && request.getMaxPrice() != null) {
                // Nhân giá trị với 1000
                BigDecimal minPrice = request.getMinPrice().multiply(BigDecimal.valueOf(1000));
                BigDecimal maxPrice = request.getMaxPrice().multiply(BigDecimal.valueOf(1000));

                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("price"), minPrice, maxPrice));
            } else if (request.getMinPrice() != null) {
                // Nhân giá trị với 1000
                BigDecimal minPrice = request.getMinPrice().multiply(BigDecimal.valueOf(1000));

                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            } else if (request.getMaxPrice() != null) {
                // Nhân giá trị với 1000
                BigDecimal maxPrice = request.getMaxPrice().multiply(BigDecimal.valueOf(1000));

                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }


            // Lọc theo productName - join với bảng Product
            if (request.getProductName() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("name")), "%" + request.getProductName().toLowerCase() + "%"));
            }

            return predicate;
        };

    }
}
