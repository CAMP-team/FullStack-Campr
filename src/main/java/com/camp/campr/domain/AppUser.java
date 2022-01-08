package com.camp.campr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    @JsonIgnoreProperties(value = { "appUser", "videos" }, allowSetters = true)
    @OneToOne(mappedBy = "appUser")
    private UserUpload userUpload;

    @JsonIgnoreProperties(value = { "appUser", "videos" }, allowSetters = true)
    @OneToOne(mappedBy = "appUser")
    private WatchHistory watchHistory;

    @JsonIgnoreProperties(value = { "appUser", "videos" }, allowSetters = true)
    @OneToOne(mappedBy = "appUser")
    private UserFavorites userFavorites;

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "video" }, allowSetters = true)
    private Set<UserComment> userComments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public AppUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public UserUpload getUserUpload() {
        return this.userUpload;
    }

    public void setUserUpload(UserUpload userUpload) {
        if (this.userUpload != null) {
            this.userUpload.setAppUser(null);
        }
        if (userUpload != null) {
            userUpload.setAppUser(this);
        }
        this.userUpload = userUpload;
    }

    public AppUser userUpload(UserUpload userUpload) {
        this.setUserUpload(userUpload);
        return this;
    }

    public WatchHistory getWatchHistory() {
        return this.watchHistory;
    }

    public void setWatchHistory(WatchHistory watchHistory) {
        if (this.watchHistory != null) {
            this.watchHistory.setAppUser(null);
        }
        if (watchHistory != null) {
            watchHistory.setAppUser(this);
        }
        this.watchHistory = watchHistory;
    }

    public AppUser watchHistory(WatchHistory watchHistory) {
        this.setWatchHistory(watchHistory);
        return this;
    }

    public UserFavorites getUserFavorites() {
        return this.userFavorites;
    }

    public void setUserFavorites(UserFavorites userFavorites) {
        if (this.userFavorites != null) {
            this.userFavorites.setAppUser(null);
        }
        if (userFavorites != null) {
            userFavorites.setAppUser(this);
        }
        this.userFavorites = userFavorites;
    }

    public AppUser userFavorites(UserFavorites userFavorites) {
        this.setUserFavorites(userFavorites);
        return this;
    }

    public Set<UserComment> getUserComments() {
        return this.userComments;
    }

    public void setUserComments(Set<UserComment> userComments) {
        if (this.userComments != null) {
            this.userComments.forEach(i -> i.setAppUser(null));
        }
        if (userComments != null) {
            userComments.forEach(i -> i.setAppUser(this));
        }
        this.userComments = userComments;
    }

    public AppUser userComments(Set<UserComment> userComments) {
        this.setUserComments(userComments);
        return this;
    }

    public AppUser addUserComment(UserComment userComment) {
        this.userComments.add(userComment);
        userComment.setAppUser(this);
        return this;
    }

    public AppUser removeUserComment(UserComment userComment) {
        this.userComments.remove(userComment);
        userComment.setAppUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            "}";
    }
}
