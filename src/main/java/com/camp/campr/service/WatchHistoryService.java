package com.camp.campr.service;

import com.camp.campr.domain.WatchHistory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link WatchHistory}.
 */
public interface WatchHistoryService {
    /**
     * Save a watchHistory.
     *
     * @param watchHistory the entity to save.
     * @return the persisted entity.
     */
    WatchHistory save(WatchHistory watchHistory);

    /**
     * Partially updates a watchHistory.
     *
     * @param watchHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WatchHistory> partialUpdate(WatchHistory watchHistory);

    /**
     * Get all the watchHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WatchHistory> findAll(Pageable pageable);

    /**
     * Get all the watchHistories with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WatchHistory> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" watchHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WatchHistory> findOne(Long id);

    /**
     * Delete the "id" watchHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
