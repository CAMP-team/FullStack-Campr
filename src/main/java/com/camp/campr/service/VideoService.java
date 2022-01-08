package com.camp.campr.service;

import com.camp.campr.domain.Video;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Video}.
 */
public interface VideoService {
    /**
     * Save a video.
     *
     * @param video the entity to save.
     * @return the persisted entity.
     */
    Video save(Video video);

    /**
     * Partially updates a video.
     *
     * @param video the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Video> partialUpdate(Video video);

    /**
     * Get all the videos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Video> findAll(Pageable pageable);

    /**
     * Get all the videos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Video> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" video.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Video> findOne(Long id);

    /**
     * Delete the "id" video.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
