package com.camp.campr.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WatchHistory.class)
public abstract class WatchHistory_ {

	public static volatile SingularAttribute<WatchHistory, Instant> dateWatched;
	public static volatile SetAttribute<WatchHistory, Video> videos;
	public static volatile SingularAttribute<WatchHistory, Long> id;
	public static volatile SingularAttribute<WatchHistory, User> user;

	public static final String DATE_WATCHED = "dateWatched";
	public static final String VIDEOS = "videos";
	public static final String ID = "id";
	public static final String USER = "user";

}

