package com.camp.campr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserFavorites.
 */
@Entity
@Table(name = "user_favorites")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserFavorites implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_added")
    private Instant dateAdded;

    @JsonIgnoreProperties(value = { "internalUser", "userUpload", "watchHistory", "userFavorites", "userComments" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private AppUser appUser;

    @ManyToMany
    @JoinTable(
        name = "rel_user_favorites__video",
        joinColumns = @JoinColumn(name = "user_favorites_id"),
        inverseJoinColumns = @JoinColumn(name = "video_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userUpload", "genres", "userComments", "watchhistories", "userfavorites" }, allowSetters = true)
    private Set<Video> videos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserFavorites id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateAdded() {
        return this.dateAdded;
    }

    public UserFavorites dateAdded(Instant dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(Instant dateAdded) {
        this.dateAdded = dateAdded;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public UserFavorites appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Set<Video> getVideos() {
        return this.videos;
    }

    public void setVideos(Set<Video> videos) {
        this.videos = videos;
    }

    public UserFavorites videos(Set<Video> videos) {
        this.setVideos(videos);
        return this;
    }

    public UserFavorites addVideo(Video video) {
        this.videos.add(video);
        video.getUserfavorites().add(this);
        return this;
    }

    public UserFavorites removeVideo(Video video) {
        this.videos.remove(video);
        video.getUserfavorites().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserFavorites)) {
            return false;
        }
        return id != null && id.equals(((UserFavorites) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserFavorites{" +
            "id=" + getId() +
            ", dateAdded='" + getDateAdded() + "'" +
            "}";
    }
}
