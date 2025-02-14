package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Brand;
import org.fpoly.capstone.service.payload.brand.BrandRequest;
import org.fpoly.capstone.service.payload.brand.BrandResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {

    List<Brand> getAllBrand();

    Page<BrandResponse> getAllBrand(Pageable pageable);

    Page<BrandResponse> searchBrand(String name, Boolean status, Pageable pageable);

    void createBrand(BrandRequest request);

    void updateBrand(Integer categoryId, BrandRequest request);

    void deleteBrand(Integer categoryId);

    BrandResponse getBrandById(Integer categoryId);

}
