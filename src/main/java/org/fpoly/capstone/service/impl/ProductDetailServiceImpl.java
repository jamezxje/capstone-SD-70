package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.repository.ProductDetailRepository;
import org.fpoly.capstone.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Override
    public List<ProductDetail> getAllProductDetails() {
        return productDetailRepository.findAll();
    }
}
