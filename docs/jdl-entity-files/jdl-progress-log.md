## January 7th, 2022
* Refer [entity-screenshots](./entity-screenshots) folder

### Progress Log
1. UserComment and AppUser connected successfully
```
entity UserComment {
    commentBody String
}

entity AppUser {
    username String
}

relationship ManyToOne {
    UserComment to AppUser
}

service all with serviceImpl
```

2. Mapped AppUser to User (special entity built into default jhipster application that cannot be modified; must extend with another entity to add additional fields)

```
entity UserComment {
    commentBody String
}

entity AppUser {
    username String
}

relationship OneToOne {
  AppUser{internalUser} to User
}

relationship ManyToOne {
    UserComment to AppUser
}

service all with serviceImpl
```

3. Added Video entity and mapped UserComment to Video (ManyToOne)
    - Also removed field from AppUser and entity still functions normally

```
entity UserComment {
    commentBody String
}

entity Video {
    title String,
    imageUrl String,
    videoUrl String,
    description String
}

entity AppUser {
    
}

relationship OneToOne {
  AppUser{internalUser} to User
}

relationship ManyToOne {
    UserComment to AppUser,
    UserComment to Video
}

service all with serviceImpl
```

4. Added Genres and mapped Genres to Video (ManyToMany)
```
entity UserComment {
	commentBody String
}

entity Video {
	title String,
    imageUrl String,
    videoUrl String,
    description String
}

entity Genre {
	name String
}

entity AppUser {
	
}

relationship OneToOne {
  AppUser{internalUser} to User
}

relationship ManyToOne {
	UserComment to AppUser,
    UserComment to Video
}

relationship ManyToMany {
	Video{genre(name)} to Genre{video}
}

service all with serviceImpl
```

5. Added UserUpload and mapped UserUpload to AppUser (OneToOne) and Video to UserUpload (ManyToOne)
```


entity UserComment {
	commentBody String
}

entity Video {
	title String,
    imageUrl String,
    videoUrl String,
    description String
}

entity Genre {
	apiId Integer,
	name String
}

entity UserUpload {
	
}

entity AppUser {
	
}

relationship OneToOne {
  AppUser{internalUser} to User,
  UserUpload to AppUser
}

relationship ManyToOne {
	UserComment to AppUser,
    UserComment to Video,
    Video to UserUpload
}

relationship ManyToMany {
	Video{genre(name)} to Genre{video}
}

service all with serviceImpl
```

6. Added WatchHistory and UserFavorites, both mapped to Video (ManyToMany)
    - WatchHistory mapped to AppUser (OneToOne)
    - UserFavorites mapped to AppUser (OneToOne)
    - Added Pagination

```

entity AppUser {
	
}

entity Video {
	title String,
    imageUrl String,
    videoUrl String,
    trailerId String,
    description String
}

entity UserComment {
	commentBody String,
    commentDate Instant
}

entity Genre {
	apiId Integer,
	name String
}

entity UserFavorites {
	dateAdded Instant
}

entity UserUpload {
	dateUploaded Instant
}

entity WatchHistory {
	dateWatched Instant
}

relationship OneToOne {
  AppUser{internalUser} to User,
  UserUpload to AppUser,
  WatchHistory to AppUser,
  UserFavorites to AppUser
}

relationship ManyToOne {
	UserComment to AppUser,
    UserComment to Video,
  	Video to UserUpload
}

relationship ManyToMany {
	Video{genre(name)} to Genre{video},
    WatchHistory{video(title)} to Video{watchhistory},
    UserFavorites{video(title)} to Video{userfavorites}
}

paginate Video, UserComment, WatchHistory, UserFavorite, UserUpload, Genre, AppUser with pagination

service all with serviceImpl
```