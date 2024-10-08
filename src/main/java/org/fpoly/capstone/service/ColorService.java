package org.fpoly.capstone.service;

import org.fpoly.capstone.service.payload.color.ColorRequest;
import org.fpoly.capstone.service.payload.color.ColorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ColorService {

  Page<ColorResponse> getAllColor(Pageable pageable);

  Page<ColorResponse> searchColors(String name, Boolean status, Pageable pageable);

  void createColor(ColorRequest request);

  ColorResponse updateColor(Integer colorId, ColorRequest request);

  void deleteColor(Integer colorId);

  ColorResponse getColorById(Integer colorId);
}
