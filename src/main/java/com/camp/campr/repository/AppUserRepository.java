package com.camp.campr.repository;

import com.camp.campr.domain.AppUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AppUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {}
