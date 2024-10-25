package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.exceptions.ResourceNotFoundException;
import org.fpoly.capstone.repository.SizeRepository;
import org.fpoly.capstone.service.SizeService;
import org.fpoly.capstone.service.payload.size.SizeRequest;
import org.fpoly.capstone.service.payload.size.SizeResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

  private final SizeRepository sizeRepository;
  private final ModelMapper modelMapper;
  private static final String SIZE_NOT_FOUND_EXCEPTIONS = "Size not found with id: ";

  @Override
  public List<Size> getAllSize() {
    return this.sizeRepository.findAll();
  }

  @Override
  public Page<SizeResponse> getAllSize(Pageable pageable) {
    return this.sizeRepository.findAll(pageable)
        .map(size -> this.modelMapper.map(size, SizeResponse.class));
  }

  @Override
  public Page<SizeResponse> searchSize(String name, Boolean status, Pageable pageable) {
    return this.sizeRepository.findByFilter(name, status, pageable);
  }

  @Override
  public void createSize(SizeRequest request) {
    Size size = Size.builder()
        .name(request.getName())
        .status(request.getStatus())
        .build();

    Size savedSize = this.sizeRepository.save(size);

    this.modelMapper.map(savedSize, SizeResponse.class);
  }

  @Override
  public void updateSize(Integer sizeId, SizeRequest request) {
    Size existingSize = this.sizeRepository.findById(Long.valueOf(sizeId))
        .orElseThrow(() -> new ResourceNotFoundException(SIZE_NOT_FOUND_EXCEPTIONS + sizeId));

    existingSize.setName(request.getName());
    existingSize.setStatus(request.getStatus());

    Size updatedSize = this.sizeRepository.save(existingSize);

    this.modelMapper.map(updatedSize, SizeResponse.class);
  }

  @Override
  public void deleteSize(Integer sizeId) {
    Size size = this.sizeRepository.findById(Long.valueOf(sizeId))
        .orElseThrow(() -> new ResourceNotFoundException(SIZE_NOT_FOUND_EXCEPTIONS + sizeId));

    this.sizeRepository.delete(size);
  }

  @Override
  public SizeResponse getSizeById(Integer sizeId) {
    Size size = this.sizeRepository.findById(Long.valueOf(sizeId))
        .orElseThrow(() -> new ResourceNotFoundException(SIZE_NOT_FOUND_EXCEPTIONS + sizeId));

    return this.modelMapper.map(size, SizeResponse.class);
  }

}
