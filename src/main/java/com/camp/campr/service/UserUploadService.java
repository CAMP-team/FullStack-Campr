package com.camp.campr.service;

import com.camp.campr.domain.UserUpload;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link UserUpload}.
 */
public interface UserUploadService {
    /**
     * Save a userUpload.
     *
     * @param userUpload the entity to save.
     * @return the persisted entity.
     */
    UserUpload save(UserUpload userUpload);

    /**
     * Partially updates a userUpload.
     *
     * @param userUpload the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserUpload> partialUpdate(UserUpload userUpload);

    /**
     * Get all the userUploads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserUpload> findAll(Pageable pageable);

    /**
     * Get the "id" userUpload.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserUpload> findOne(Long id);

    /**
     * Delete the "id" userUpload.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
