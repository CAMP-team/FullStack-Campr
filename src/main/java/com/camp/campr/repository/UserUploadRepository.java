package com.camp.campr.repository;

import com.camp.campr.domain.UserUpload;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserUpload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserUploadRepository extends JpaRepository<UserUpload, Long> {}
