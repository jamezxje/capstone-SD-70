package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.exceptions.ResourceNotFoundException;
import org.fpoly.capstone.repository.ColorRepository;
import org.fpoly.capstone.service.ColorService;
import org.fpoly.capstone.service.payload.color.ColorRequest;
import org.fpoly.capstone.service.payload.color.ColorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

  private final ColorRepository colorRepository;
  private final ModelMapper modelMapper;

  @Override
  public Page<ColorResponse> getAllColor(Pageable pageable) {
    return this.colorRepository.findAll(pageable)
        .map(color -> this.modelMapper.map(color, ColorResponse.class));
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
  public ColorResponse updateColor(Integer colorId, ColorRequest request) {
    Color existingColor = this.colorRepository.findById(Long.valueOf(colorId))
        .orElseThrow(() -> new ResourceNotFoundException("Color not found with id: " + colorId));

    existingColor.setName(request.getName());
    existingColor.setStatus(request.getStatus());

    Color updatedColor = this.colorRepository.save(existingColor);

    return this.modelMapper.map(updatedColor, ColorResponse.class);
  }

  @Override
  public void deleteColor(Integer colorId) {
    Color color = this.colorRepository.findById(Long.valueOf(colorId))
        .orElseThrow(() -> new ResourceNotFoundException("Color not found with id: " + colorId));

    this.colorRepository.delete(color);
  }

  @Override
  public ColorResponse getColorById(Integer colorId) {
    Color color = this.colorRepository.findById(Long.valueOf(colorId))
        .orElseThrow(() -> new ResourceNotFoundException("Color not found with id: " + colorId));

    return this.modelMapper.map(color, ColorResponse.class);
  }

}
