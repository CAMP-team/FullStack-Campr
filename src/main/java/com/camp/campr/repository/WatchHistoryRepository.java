package com.camp.campr.repository;

import com.camp.campr.domain.WatchHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WatchHistory entity.
 */
@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    @Query("select watchHistory from WatchHistory watchHistory where watchHistory.user.login = ?#{principal.username}")
    List<WatchHistory> findByUserIsCurrentUser();

    @Query(
        value = "select distinct watchHistory from WatchHistory watchHistory left join fetch watchHistory.videos",
        countQuery = "select count(distinct watchHistory) from WatchHistory watchHistory"
    )
    Page<WatchHistory> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct watchHistory from WatchHistory watchHistory left join fetch watchHistory.videos")
    List<WatchHistory> findAllWithEagerRelationships();

    @Query("select watchHistory from WatchHistory watchHistory left join fetch watchHistory.videos where watchHistory.id =:id")
    Optional<WatchHistory> findOneWithEagerRelationships(@Param("id") Long id);
}
