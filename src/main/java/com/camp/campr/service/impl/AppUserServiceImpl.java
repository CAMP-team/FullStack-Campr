package com.camp.campr.service.impl;

import com.camp.campr.domain.AppUser;
import com.camp.campr.repository.AppUserRepository;
import com.camp.campr.service.AppUserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AppUser}.
 */
@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser save(AppUser appUser) {
        log.debug("Request to save AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    @Override
    public Optional<AppUser> partialUpdate(AppUser appUser) {
        log.debug("Request to partially update AppUser : {}", appUser);

        return appUserRepository
            .findById(appUser.getId())
            .map(existingAppUser -> {
                return existingAppUser;
            })
            .map(appUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppUser> findAll(Pageable pageable) {
        log.debug("Request to get all AppUsers");
        return appUserRepository.findAll(pageable);
    }

    /**
     *  Get all the appUsers where UserUpload is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppUser> findAllWhereUserUploadIsNull() {
        log.debug("Request to get all appUsers where UserUpload is null");
        return StreamSupport
            .stream(appUserRepository.findAll().spliterator(), false)
            .filter(appUser -> appUser.getUserUpload() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the appUsers where WatchHistory is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppUser> findAllWhereWatchHistoryIsNull() {
        log.debug("Request to get all appUsers where WatchHistory is null");
        return StreamSupport
            .stream(appUserRepository.findAll().spliterator(), false)
            .filter(appUser -> appUser.getWatchHistory() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the appUsers where UserFavorites is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppUser> findAllWhereUserFavoritesIsNull() {
        log.debug("Request to get all appUsers where UserFavorites is null");
        return StreamSupport
            .stream(appUserRepository.findAll().spliterator(), false)
            .filter(appUser -> appUser.getUserFavorites() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUser> findOne(Long id) {
        log.debug("Request to get AppUser : {}", id);
        return appUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
    }
}
