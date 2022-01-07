## January 5th, 2022
* Looking into AWS S3 for video uploads, so we can get a Url to display on our app

## January 4th, 2022
* Generated app via jhipster and successfully deployed to Heroku
1. First ran into issues trying to create a jhipster app via the `jhipster heroku` option, so instead just created a jhipster app through the typical route using `jhipster`
2. Then ran into issue deploying GitHub branch with project to Heroku due to Maven java runtime version
2. Fix included adding a `system.properties` specifying the java runtime version such as `java.runtime.version=11`

## January 3rd, 2022
* CAMP-team: Carnell, Amanda, Mike, Paul

* **Planning Notes**:
    * Database: PostGres
    * Deploy on Heroku asap
    * Spring+Angular/React+Db OR jhipster
        - Jhipster: https://start.jhipster.tech/generate-application
        - Spring Initializr: https://start.spring.io/
    * Need to decide if we are saving movies/tv data to the db or just calling via the API
        - API: https://www.themoviedb.org/documentation/api

* Database Tables
    * Users
        - id: auto_incremented integer not null
        - name: varchar(255)
        - email: varchar(255)
        - pwd: varchar(255)
        - myList integer id[]
    * Movies&TV
        - id: auto_incremented integer not null
        - movie/show id integer
        - name varchar(255)
        - genre: array[object]
        - images
    * Genres
