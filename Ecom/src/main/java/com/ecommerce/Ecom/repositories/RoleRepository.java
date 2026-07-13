package com.ecommerce.Ecom.repositories;


import com.ecommerce.Ecom.model.AppRole;
import com.ecommerce.Ecom.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

   Optional<Role> findByRoleName(AppRole appRole);
}
