package com.camp.campr.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserUpload.class)
public abstract class UserUpload_ {

	public static volatile SingularAttribute<UserUpload, Instant> dateUploaded;
	public static volatile SetAttribute<UserUpload, Video> videos;
	public static volatile SingularAttribute<UserUpload, Long> id;
	public static volatile SingularAttribute<UserUpload, User> user;

	public static final String DATE_UPLOADED = "dateUploaded";
	public static final String VIDEOS = "videos";
	public static final String ID = "id";
	public static final String USER = "user";

}

