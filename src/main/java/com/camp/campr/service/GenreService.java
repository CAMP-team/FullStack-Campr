package com.camp.campr.service;

import com.camp.campr.domain.Genre;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Genre}.
 */
public interface GenreService {
    /**
     * Save a genre.
     *
     * @param genre the entity to save.
     * @return the persisted entity.
     */
    Genre save(Genre genre);

    /**
     * Partially updates a genre.
     *
     * @param genre the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Genre> partialUpdate(Genre genre);

    /**
     * Get all the genres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Genre> findAll(Pageable pageable);

    /**
     * Get the "id" genre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Genre> findOne(Long id);

    /**
     * Delete the "id" genre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
