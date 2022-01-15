package com.camp.campr.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserFavorites.class)
public abstract class UserFavorites_ {

	public static volatile SetAttribute<UserFavorites, Video> videos;
	public static volatile SingularAttribute<UserFavorites, Long> id;
	public static volatile SingularAttribute<UserFavorites, User> user;
	public static volatile SingularAttribute<UserFavorites, Instant> dateAdded;

	public static final String VIDEOS = "videos";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String DATE_ADDED = "dateAdded";

}

