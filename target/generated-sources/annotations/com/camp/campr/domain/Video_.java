package com.camp.campr.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Video.class)
public abstract class Video_ {

	public static volatile SingularAttribute<Video, String> videoUrl;
	public static volatile SetAttribute<Video, Genre> genres;
	public static volatile SingularAttribute<Video, String> imageUrl;
	public static volatile SingularAttribute<Video, UserUpload> userUpload;
	public static volatile SingularAttribute<Video, String> description;
	public static volatile SetAttribute<Video, UserFavorites> userfavorites;
	public static volatile SingularAttribute<Video, Long> id;
	public static volatile SingularAttribute<Video, String> title;
	public static volatile SingularAttribute<Video, String> trailerId;
	public static volatile SetAttribute<Video, UserComment> userComments;
	public static volatile SetAttribute<Video, WatchHistory> watchhistories;

	public static final String VIDEO_URL = "videoUrl";
	public static final String GENRES = "genres";
	public static final String IMAGE_URL = "imageUrl";
	public static final String USER_UPLOAD = "userUpload";
	public static final String DESCRIPTION = "description";
	public static final String USERFAVORITES = "userfavorites";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String TRAILER_ID = "trailerId";
	public static final String USER_COMMENTS = "userComments";
	public static final String WATCHHISTORIES = "watchhistories";

}

