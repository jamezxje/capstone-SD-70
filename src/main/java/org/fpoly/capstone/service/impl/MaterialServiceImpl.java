package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.exceptions.ResourceNotFoundException;
import org.fpoly.capstone.repository.MaterialRepository;
import org.fpoly.capstone.service.MaterialService;
import org.fpoly.capstone.service.payload.material.MaterialRequest;
import org.fpoly.capstone.service.payload.material.MaterialResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

  private final MaterialRepository materialRepository;
  private final ModelMapper modelMapper;
  private static final String MATERIAL_NOT_FOUND_EXCEPTIONS = "Material not found with id: ";

  @Override
  public List<Material> getAllMaterial() {
    return this.materialRepository.findAll();
  }

  @Override
  public Page<MaterialResponse> getAllMaterial(Pageable pageable) {
    return this.materialRepository.findAll(pageable)
        .map(material -> this.modelMapper.map(material, MaterialResponse.class));
  }

  @Override
  public Page<MaterialResponse> searchMaterial(String name, Boolean status, Pageable pageable) {
    // Delegate the search logic to the repository's custom query method
    return this.materialRepository.findByFilter(name, status, pageable);
  }

  @Override
  public void createMaterial(MaterialRequest request) {
    Material material = Material.builder()
        .name(request.getName())
        .status(request.getStatus())
        .build();

    Material savedMaterial = this.materialRepository.save(material);

    this.modelMapper.map(savedMaterial, MaterialResponse.class);
  }

  @Override
  public void updateMaterial(Integer materialId, MaterialRequest request) {
    Material existingMaterial = this.materialRepository.findById(Long.valueOf(materialId))
        .orElseThrow(() -> new ResourceNotFoundException(MATERIAL_NOT_FOUND_EXCEPTIONS + materialId));

    existingMaterial.setName(request.getName());
    existingMaterial.setStatus(request.getStatus());

    Material updatedMaterial = this.materialRepository.save(existingMaterial);

    this.modelMapper.map(updatedMaterial, MaterialResponse.class);
  }

  @Override
  public void deleteMaterial(Integer materialId) {
    Material material = this.materialRepository.findById(Long.valueOf(materialId))
        .orElseThrow(() -> new ResourceNotFoundException(MATERIAL_NOT_FOUND_EXCEPTIONS + materialId));

    this.materialRepository.delete(material);
  }

  @Override
  public MaterialResponse getMaterialById(Integer materialId) {
    Material material = this.materialRepository.findById(Long.valueOf(materialId))
        .orElseThrow(() -> new ResourceNotFoundException(MATERIAL_NOT_FOUND_EXCEPTIONS + materialId));

    return this.modelMapper.map(material, MaterialResponse.class);
  }

}
