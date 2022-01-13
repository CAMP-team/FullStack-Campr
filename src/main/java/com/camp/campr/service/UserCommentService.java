package com.camp.campr.service;

import com.camp.campr.domain.UserComment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link UserComment}.
 */
public interface UserCommentService {
    /**
     * Save a userComment.
     *
     * @param userComment the entity to save.
     * @return the persisted entity.
     */
    UserComment save(UserComment userComment);

    /**
     * Partially updates a userComment.
     *
     * @param userComment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserComment> partialUpdate(UserComment userComment);

    /**
     * Get all the userComments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserComment> findAll(Pageable pageable);

    /**
     * Get the "id" userComment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserComment> findOne(Long id);

    /**
     * Delete the "id" userComment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<UserComment> findByAppUser(String appUser, Pageable pageable);
}
