package com.Firmanann.CoreBankingSystem.roles.repository;

import com.Firmanann.CoreBankingSystem.roles.entity.RoleEntity;
import com.Firmanann.CoreBankingSystem.roles.entity.RolesStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByStatus(RolesStatus name);

}
