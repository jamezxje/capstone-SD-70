package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Brand;
import org.fpoly.capstone.exceptions.ResourceNotFoundException;
import org.fpoly.capstone.repository.BrandRepository;
import org.fpoly.capstone.service.BrandService;
import org.fpoly.capstone.service.payload.brand.BrandRequest;
import org.fpoly.capstone.service.payload.brand.BrandResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;
    private static final String BRAND_NOT_FOUND_WITH_ID = "Brand not found with id: ";

    @Override
    public List<Brand> getAllBrand() {
        return this.brandRepository.findAll();
    }

    @Override
    public Page<BrandResponse> getAllBrand(Pageable pageable) {
        return this.brandRepository.findAll(pageable)
                .map(brand -> this.modelMapper.map(brand, BrandResponse.class));
    }

    @Override
    public Page<BrandResponse> searchBrand(String name, Boolean status, Pageable pageable) {
        return this.brandRepository.findByFilter(name, status, pageable);
    }

    @Override
    public void createBrand(BrandRequest request) {
        Brand brand = Brand.builder()
                .name(request.getName())
                .status(request.getStatus())
                .build();

        Brand savedBrand = this.brandRepository.save(brand);

        this.modelMapper.map(savedBrand, BrandResponse.class);
    }

    @Override
    public void updateBrand(Integer brandId, BrandRequest request) {
        Brand existingBrand = this.brandRepository.findById(Long.valueOf(brandId))
                .orElseThrow(() -> new ResourceNotFoundException(BRAND_NOT_FOUND_WITH_ID + brandId));

        existingBrand.setName(request.getName());
        existingBrand.setStatus(request.getStatus());

        Brand updatedBrand = this.brandRepository.save(existingBrand);

        this.modelMapper.map(updatedBrand, BrandResponse.class);
    }

    @Override
    public void deleteBrand(Integer brandId) {
        Brand brand = this.brandRepository.findById(Long.valueOf(brandId))
                .orElseThrow(() -> new ResourceNotFoundException(BRAND_NOT_FOUND_WITH_ID + brandId));

        this.brandRepository.delete(brand);
    }

    @Override
    public BrandResponse getBrandById(Integer brandId) {
        Brand brand = this.brandRepository.findById(Long.valueOf(brandId))
                .orElseThrow(() -> new ResourceNotFoundException(BRAND_NOT_FOUND_WITH_ID + brandId));

        return this.modelMapper.map(brand, BrandResponse.class);
    }

}
