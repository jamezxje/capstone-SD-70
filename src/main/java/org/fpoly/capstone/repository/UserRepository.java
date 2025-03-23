package org.fpoly.capstone.repository;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.fpoly.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

//    boolean existsByEmail(@NotEmpty(message = "Vui lòng không để trống email") @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9._%+-]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",message = "Email không đúng định dạng") String email);
}
