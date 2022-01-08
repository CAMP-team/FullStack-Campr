package com.camp.campr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Genre.
 */
@Entity
@Table(name = "genre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "api_id")
    private Integer apiId;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userUpload", "genres", "userComments", "watchhistories", "userfavorites" }, allowSetters = true)
    private Set<Video> videos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Genre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getApiId() {
        return this.apiId;
    }

    public Genre apiId(Integer apiId) {
        this.setApiId(apiId);
        return this;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return this.name;
    }

    public Genre name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Video> getVideos() {
        return this.videos;
    }

    public void setVideos(Set<Video> videos) {
        if (this.videos != null) {
            this.videos.forEach(i -> i.removeGenre(this));
        }
        if (videos != null) {
            videos.forEach(i -> i.addGenre(this));
        }
        this.videos = videos;
    }

    public Genre videos(Set<Video> videos) {
        this.setVideos(videos);
        return this;
    }

    public Genre addVideo(Video video) {
        this.videos.add(video);
        video.getGenres().add(this);
        return this;
    }

    public Genre removeVideo(Video video) {
        this.videos.remove(video);
        video.getGenres().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Genre)) {
            return false;
        }
        return id != null && id.equals(((Genre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Genre{" +
            "id=" + getId() +
            ", apiId=" + getApiId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
