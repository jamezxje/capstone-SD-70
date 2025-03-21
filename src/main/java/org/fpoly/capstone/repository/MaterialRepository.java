package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material , Long> {
}
