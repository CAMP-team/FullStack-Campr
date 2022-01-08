package com.camp.campr.service.impl;

import com.camp.campr.domain.UserFavorites;
import com.camp.campr.repository.UserFavoritesRepository;
import com.camp.campr.service.UserFavoritesService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserFavorites}.
 */
@Service
@Transactional
public class UserFavoritesServiceImpl implements UserFavoritesService {

    private final Logger log = LoggerFactory.getLogger(UserFavoritesServiceImpl.class);

    private final UserFavoritesRepository userFavoritesRepository;

    public UserFavoritesServiceImpl(UserFavoritesRepository userFavoritesRepository) {
        this.userFavoritesRepository = userFavoritesRepository;
    }

    @Override
    public UserFavorites save(UserFavorites userFavorites) {
        log.debug("Request to save UserFavorites : {}", userFavorites);
        return userFavoritesRepository.save(userFavorites);
    }

    @Override
    public Optional<UserFavorites> partialUpdate(UserFavorites userFavorites) {
        log.debug("Request to partially update UserFavorites : {}", userFavorites);

        return userFavoritesRepository
            .findById(userFavorites.getId())
            .map(existingUserFavorites -> {
                if (userFavorites.getDateAdded() != null) {
                    existingUserFavorites.setDateAdded(userFavorites.getDateAdded());
                }

                return existingUserFavorites;
            })
            .map(userFavoritesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserFavorites> findAll(Pageable pageable) {
        log.debug("Request to get all UserFavorites");
        return userFavoritesRepository.findAll(pageable);
    }

    public Page<UserFavorites> findAllWithEagerRelationships(Pageable pageable) {
        return userFavoritesRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserFavorites> findOne(Long id) {
        log.debug("Request to get UserFavorites : {}", id);
        return userFavoritesRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserFavorites : {}", id);
        userFavoritesRepository.deleteById(id);
    }
}
