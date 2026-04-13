package com.embarkx.sbecommerce.repository;

import com.embarkx.sbecommerce.model.AppRole;
import com.embarkx.sbecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(AppRole role);
}
