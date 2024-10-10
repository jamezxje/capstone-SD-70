package org.fpoly.capstone.service;

import org.fpoly.capstone.service.payload.size.SizeRequest;
import org.fpoly.capstone.service.payload.size.SizeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SizeService {

  Page<SizeResponse> getAllSize(Pageable pageable);

  Page<SizeResponse> searchSize(String name, Boolean status, Pageable pageable);

  void createSize(SizeRequest request);

  void updateSize(Integer sizeId, SizeRequest request);

  void deleteSize(Integer sizeId);

  SizeResponse getSizeById(Integer sizeId);

}
