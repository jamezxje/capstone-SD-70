package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category , Long> {

}
