package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.service.payload.material.MaterialRequest;
import org.fpoly.capstone.service.payload.material.MaterialResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaterialService {

  List<Material> getAllMaterial();

  Page<MaterialResponse> getAllMaterial(Pageable pageable);

  Page<MaterialResponse> searchMaterial(String name, Boolean status, Pageable pageable);

  void createMaterial(MaterialRequest request);

  void updateMaterial(Integer materialId, MaterialRequest request);

  void deleteMaterial(Integer materialId);

  MaterialResponse getMaterialById(Integer materialId);

}
