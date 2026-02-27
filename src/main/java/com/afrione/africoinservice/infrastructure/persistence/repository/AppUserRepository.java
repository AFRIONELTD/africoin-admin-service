package com.afrione.africoinservice.infrastructure.persistence.repository;


import com.afrione.africoinservice.domain.entities.AppUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    boolean existsByEmail(String email);
    Optional<AppUserEntity> findByEmail(String email);
    Optional<AppUserEntity> findByPhoneNumber(String userId);

    @Query("SELECT u FROM AppUserEntity u JOIN FETCH u.roles WHERE u.id = :id")
    Optional<AppUserEntity> findByIdWithRoles(@Param("id") Long id);

    @Query(value = "SELECT DISTINCT u FROM AppUserEntity u LEFT JOIN u.roles r " +
            "WHERE (:searchTerm IS NULL OR (LOWER(u.firstName) LIKE CONCAT('%', LOWER(:searchTerm), '%') " +
            "OR LOWER(u.lastName) LIKE CONCAT('%', LOWER(:searchTerm), '%') " +
            "OR LOWER(u.email) LIKE CONCAT('%', LOWER(:searchTerm), '%') " +
            "OR u.phoneNumber LIKE CONCAT('%', :searchTerm, '%'))) " +
            "AND (:roleId IS NULL OR r.id = :roleId)")
    Page<AppUserEntity> findAllUsersFiltered(@Param("searchTerm") String searchTerm, @Param("roleId") Long roleId, org.springframework.data.domain.Pageable pageable);

}
