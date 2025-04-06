package org.fpoly.capstone.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailFilterRequest;
import org.springframework.data.jpa.domain.Specification;

public class ProductDetailSpecification {

    public static Specification<ProductDetail> filterByRequest(ProductDetailFilterRequest request) {

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.getProductId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("product").get("id"), request.getProductId()));
            }

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
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            // Lọc theo giá
            if (request.getMinPrice() != null && request.getMaxPrice() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("price"), request.getMinPrice(), request.getMaxPrice()));
            } else if (request.getMinPrice() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
            } else if (request.getMaxPrice() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }

            // Lọc theo productName - join với bảng Product
            if (request.getProductName() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("name")), "%" + request.getProductName().toLowerCase() + "%"));
            }

            // Set the order by createDate in descending order
            query.orderBy(criteriaBuilder.desc(root.get("createDate")));

            return predicate;
        };

    }
}
