## January 4th, 2022
* Generated app via jhipster and successfully deployed to Heroku

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
