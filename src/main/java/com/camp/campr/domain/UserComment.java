package com.camp.campr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserComment.
 */
@Entity
@Table(name = "user_comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "comment_body")
    private String commentBody;

    @Column(name = "comment_date")
    private Instant commentDate;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "userUpload", "genres", "userComments", "watchhistories", "userfavorites" }, allowSetters = true)
    private Video video;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserComment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentBody() {
        return this.commentBody;
    }

    public UserComment commentBody(String commentBody) {
        this.setCommentBody(commentBody);
        return this;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Instant getCommentDate() {
        return this.commentDate;
    }

    public UserComment commentDate(Instant commentDate) {
        this.setCommentDate(commentDate);
        return this;
    }

    public void setCommentDate(Instant commentDate) {
        this.commentDate = commentDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserComment user(User user) {
        this.setUser(user);
        return this;
    }

    public Video getVideo() {
        return this.video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public UserComment video(Video video) {
        this.setVideo(video);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserComment)) {
            return false;
        }
        return id != null && id.equals(((UserComment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserComment{" +
            "id=" + getId() +
            ", commentBody='" + getCommentBody() + "'" +
            ", commentDate='" + getCommentDate() + "'" +
            "}";
    }
}
