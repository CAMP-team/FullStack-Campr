package com.camp.campr.service.impl;

import com.camp.campr.domain.Video;
import com.camp.campr.repository.VideoRepository;
import com.camp.campr.service.VideoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Video}.
 */
@Service
@Transactional
public class VideoServiceImpl implements VideoService {

    private final Logger log = LoggerFactory.getLogger(VideoServiceImpl.class);

    private final VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public Video save(Video video) {
        log.debug("Request to save Video : {}", video);
        return videoRepository.save(video);
    }

    @Override
    public Optional<Video> partialUpdate(Video video) {
        log.debug("Request to partially update Video : {}", video);

        return videoRepository
            .findById(video.getId())
            .map(existingVideo -> {
                if (video.getTitle() != null) {
                    existingVideo.setTitle(video.getTitle());
                }
                if (video.getImageUrl() != null) {
                    existingVideo.setImageUrl(video.getImageUrl());
                }
                if (video.getVideoUrl() != null) {
                    existingVideo.setVideoUrl(video.getVideoUrl());
                }
                if (video.getTrailerId() != null) {
                    existingVideo.setTrailerId(video.getTrailerId());
                }
                if (video.getDescription() != null) {
                    existingVideo.setDescription(video.getDescription());
                }

                return existingVideo;
            })
            .map(videoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Video> findAll(Pageable pageable) {
        log.debug("Request to get all Videos");
        return videoRepository.findAll(pageable);
    }

    public Page<Video> findAllWithEagerRelationships(Pageable pageable) {
        return videoRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findOne(Long id) {
        log.debug("Request to get Video : {}", id);
        return videoRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Video : {}", id);
        videoRepository.deleteById(id);
    }
}
