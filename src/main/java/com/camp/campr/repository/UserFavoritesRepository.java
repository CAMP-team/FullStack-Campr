package com.camp.campr.repository;

import com.camp.campr.domain.UserFavorites;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserFavorites entity.
 */
@Repository
public interface UserFavoritesRepository extends JpaRepository<UserFavorites, Long> {
    @Query(
        value = "select distinct userFavorites from UserFavorites userFavorites left join fetch userFavorites.videos",
        countQuery = "select count(distinct userFavorites) from UserFavorites userFavorites"
    )
    Page<UserFavorites> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userFavorites from UserFavorites userFavorites left join fetch userFavorites.videos")
    List<UserFavorites> findAllWithEagerRelationships();

    @Query("select userFavorites from UserFavorites userFavorites left join fetch userFavorites.videos where userFavorites.id =:id")
    Optional<UserFavorites> findOneWithEagerRelationships(@Param("id") Long id);
}
