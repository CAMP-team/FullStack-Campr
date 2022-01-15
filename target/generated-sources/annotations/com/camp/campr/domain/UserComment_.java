package com.camp.campr.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserComment.class)
public abstract class UserComment_ {

	public static volatile SingularAttribute<UserComment, Instant> commentDate;
	public static volatile SingularAttribute<UserComment, String> commentBody;
	public static volatile SingularAttribute<UserComment, Long> id;
	public static volatile SingularAttribute<UserComment, Video> video;
	public static volatile SingularAttribute<UserComment, User> user;

	public static final String COMMENT_DATE = "commentDate";
	public static final String COMMENT_BODY = "commentBody";
	public static final String ID = "id";
	public static final String VIDEO = "video";
	public static final String USER = "user";

}

