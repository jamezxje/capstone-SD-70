package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.exceptions.ResourceNotFoundException;
import org.fpoly.capstone.repository.ColorRepository;
import org.fpoly.capstone.service.ColorService;
import org.fpoly.capstone.service.payload.color.ColorRequest;
import org.fpoly.capstone.service.payload.color.ColorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

  private final ColorRepository colorRepository;
  private final ModelMapper modelMapper;
  private static final String COLOR_NOT_FOUND_EXCEPTIONS = "Color not found with id: ";

  @Override
  public Page<ColorResponse> getAllColor(Pageable pageable) {
    return this.colorRepository.findAll(pageable)
        .map(color -> this.modelMapper.map(color, ColorResponse.class));
  }

  @Override
  public Page<ColorResponse> searchColor(String name, Boolean status, Pageable pageable) {
    // Delegate the search logic to the repository's custom query method
    return this.colorRepository.findByFilter(name, status, pageable);
  }

  @Override
  public void createColor(ColorRequest request) {
    Color color = Color.builder()
        .name(request.getName())
        .status(request.getStatus())
        .build();

    Color savedColor = this.colorRepository.save(color);

    this.modelMapper.map(savedColor, ColorResponse.class);
  }

  @Override
  public void updateColor(Integer colorId, ColorRequest request) {
    Color existingColor = this.colorRepository.findById(Long.valueOf(colorId))
        .orElseThrow(() -> new ResourceNotFoundException(COLOR_NOT_FOUND_EXCEPTIONS + colorId));

    existingColor.setName(request.getName());
    existingColor.setStatus(request.getStatus());

    Color updatedColor = this.colorRepository.save(existingColor);

    this.modelMapper.map(updatedColor, ColorResponse.class);
  }

  @Override
  public void deleteColor(Integer colorId) {
    Color color = this.colorRepository.findById(Long.valueOf(colorId))
        .orElseThrow(() -> new ResourceNotFoundException(COLOR_NOT_FOUND_EXCEPTIONS + colorId));

    this.colorRepository.delete(color);
  }

  @Override
  public ColorResponse getColorById(Integer colorId) {
    Color color = this.colorRepository.findById(Long.valueOf(colorId))
        .orElseThrow(() -> new ResourceNotFoundException(COLOR_NOT_FOUND_EXCEPTIONS + colorId));

    return this.modelMapper.map(color, ColorResponse.class);
  }

}
