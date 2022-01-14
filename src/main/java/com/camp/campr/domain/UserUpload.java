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
 * A UserUpload.
 */
@Entity
@Table(name = "user_upload")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_uploaded")
    private Instant dateUploaded;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "userUpload")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userUpload", "genres", "userComments", "watchhistories", "userfavorites" }, allowSetters = true)
    private Set<Video> videos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserUpload id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateUploaded() {
        return this.dateUploaded;
    }

    public UserUpload dateUploaded(Instant dateUploaded) {
        this.setDateUploaded(dateUploaded);
        return this;
    }

    public void setDateUploaded(Instant dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserUpload user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Video> getVideos() {
        return this.videos;
    }

    public void setVideos(Set<Video> videos) {
        if (this.videos != null) {
            this.videos.forEach(i -> i.setUserUpload(null));
        }
        if (videos != null) {
            videos.forEach(i -> i.setUserUpload(this));
        }
        this.videos = videos;
    }

    public UserUpload videos(Set<Video> videos) {
        this.setVideos(videos);
        return this;
    }

    public UserUpload addVideo(Video video) {
        this.videos.add(video);
        video.setUserUpload(this);
        return this;
    }

    public UserUpload removeVideo(Video video) {
        this.videos.remove(video);
        video.setUserUpload(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserUpload)) {
            return false;
        }
        return id != null && id.equals(((UserUpload) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserUpload{" +
            "id=" + getId() +
            ", dateUploaded='" + getDateUploaded() + "'" +
            "}";
    }
}
