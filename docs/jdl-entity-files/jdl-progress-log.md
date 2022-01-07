## January 7th, 2022
* Refer [entity-screenshots] folder

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