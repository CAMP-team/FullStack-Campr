package com.camp.campr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Video.
 */
@Entity
@Table(name = "video")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "trailer_id")
    private String trailerId;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "videos" }, allowSetters = true)
    private UserUpload userUpload;

    @ManyToMany
    @JoinTable(name = "rel_video__genre", joinColumns = @JoinColumn(name = "video_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "videos" }, allowSetters = true)
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "video")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "video" }, allowSetters = true)
    private Set<UserComment> userComments = new HashSet<>();

    @ManyToMany(mappedBy = "videos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "videos" }, allowSetters = true)
    private Set<WatchHistory> watchhistories = new HashSet<>();

    @ManyToMany(mappedBy = "videos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "videos" }, allowSetters = true)
    private Set<UserFavorites> userfavorites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Video id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Video title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Video imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public Video videoUrl(String videoUrl) {
        this.setVideoUrl(videoUrl);
        return this;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTrailerId() {
        return this.trailerId;
    }

    public Video trailerId(String trailerId) {
        this.setTrailerId(trailerId);
        return this;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getDescription() {
        return this.description;
    }

    public Video description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserUpload getUserUpload() {
        return this.userUpload;
    }

    public void setUserUpload(UserUpload userUpload) {
        this.userUpload = userUpload;
    }

    public Video userUpload(UserUpload userUpload) {
        this.setUserUpload(userUpload);
        return this;
    }

    public Set<Genre> getGenres() {
        return this.genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Video genres(Set<Genre> genres) {
        this.setGenres(genres);
        return this;
    }

    public Video addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getVideos().add(this);
        return this;
    }

    public Video removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getVideos().remove(this);
        return this;
    }

    public Set<UserComment> getUserComments() {
        return this.userComments;
    }

    public void setUserComments(Set<UserComment> userComments) {
        if (this.userComments != null) {
            this.userComments.forEach(i -> i.setVideo(null));
        }
        if (userComments != null) {
            userComments.forEach(i -> i.setVideo(this));
        }
        this.userComments = userComments;
    }

    public Video userComments(Set<UserComment> userComments) {
        this.setUserComments(userComments);
        return this;
    }

    public Video addUserComment(UserComment userComment) {
        this.userComments.add(userComment);
        userComment.setVideo(this);
        return this;
    }

    public Video removeUserComment(UserComment userComment) {
        this.userComments.remove(userComment);
        userComment.setVideo(null);
        return this;
    }

    public Set<WatchHistory> getWatchhistories() {
        return this.watchhistories;
    }

    public void setWatchhistories(Set<WatchHistory> watchHistories) {
        if (this.watchhistories != null) {
            this.watchhistories.forEach(i -> i.removeVideo(this));
        }
        if (watchHistories != null) {
            watchHistories.forEach(i -> i.addVideo(this));
        }
        this.watchhistories = watchHistories;
    }

    public Video watchhistories(Set<WatchHistory> watchHistories) {
        this.setWatchhistories(watchHistories);
        return this;
    }

    public Video addWatchhistory(WatchHistory watchHistory) {
        this.watchhistories.add(watchHistory);
        watchHistory.getVideos().add(this);
        return this;
    }

    public Video removeWatchhistory(WatchHistory watchHistory) {
        this.watchhistories.remove(watchHistory);
        watchHistory.getVideos().remove(this);
        return this;
    }

    public Set<UserFavorites> getUserfavorites() {
        return this.userfavorites;
    }

    public void setUserfavorites(Set<UserFavorites> userFavorites) {
        if (this.userfavorites != null) {
            this.userfavorites.forEach(i -> i.removeVideo(this));
        }
        if (userFavorites != null) {
            userFavorites.forEach(i -> i.addVideo(this));
        }
        this.userfavorites = userFavorites;
    }

    public Video userfavorites(Set<UserFavorites> userFavorites) {
        this.setUserfavorites(userFavorites);
        return this;
    }

    public Video addUserfavorites(UserFavorites userFavorites) {
        this.userfavorites.add(userFavorites);
        userFavorites.getVideos().add(this);
        return this;
    }

    public Video removeUserfavorites(UserFavorites userFavorites) {
        this.userfavorites.remove(userFavorites);
        userFavorites.getVideos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Video)) {
            return false;
        }
        return id != null && id.equals(((Video) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Video{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", trailerId='" + getTrailerId() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
