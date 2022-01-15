package com.camp.campr.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AppUser.class)
public abstract class AppUser_ {

	public static volatile SingularAttribute<AppUser, User> internalUser;
	public static volatile SingularAttribute<AppUser, UserUpload> userUpload;
	public static volatile SingularAttribute<AppUser, WatchHistory> watchHistory;
	public static volatile SingularAttribute<AppUser, Long> id;
	public static volatile SingularAttribute<AppUser, UserFavorites> userFavorites;
	public static volatile SetAttribute<AppUser, UserComment> userComments;

	public static final String INTERNAL_USER = "internalUser";
	public static final String USER_UPLOAD = "userUpload";
	public static final String WATCH_HISTORY = "watchHistory";
	public static final String ID = "id";
	public static final String USER_FAVORITES = "userFavorites";
	public static final String USER_COMMENTS = "userComments";

}

