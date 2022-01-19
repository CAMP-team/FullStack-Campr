package com.camp.campr.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Genre.class)
public abstract class Genre_ {

	public static volatile SingularAttribute<Genre, String> name;
	public static volatile SetAttribute<Genre, Video> videos;
	public static volatile SingularAttribute<Genre, Long> id;
	public static volatile SingularAttribute<Genre, Integer> apiId;

	public static final String NAME = "name";
	public static final String VIDEOS = "videos";
	public static final String ID = "id";
	public static final String API_ID = "apiId";

}

