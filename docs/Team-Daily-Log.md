## January 13th-14th, 2022
* Reloaded project with proper entity implementation
* Configured entity getAll methods to retrieve user account items for current logged in user
* Fast-forwarded most changes and pushed to dev > should deploy successfully
  * Downloaded pgAdmin to directly edit Heroku PostgreSQL data
    - Need to configure permissions > currently getting the following error:
    ```
    connection to server at "ec2-3-223-39-179.compute-1.amazonaws.com" (3.223.39.179), port 5432 failed: FATAL: permission denied for database "d1a3r6ta66v5qn" DETAIL: User does not have CONNECT privilege.
    ```
* Will need to figure out how to hide videos and genres from users drop-down
* 500 Internal Server Error > resolution steps:
    - Debugged by cd Dev/Projects/FullStack-Campr
    - heroku logs --tail --app camp-r
    - at... lines don't matter as much, try to find lines that specify an exact error and where it's failing
    - checked localhost:8080 for project h2 database > worked fine, meaning the project file is fine
    - checked Heroku PostgreSQL db through pgAdmin
        - campr > database > schema > table > columns
        - updated columns labeled app_user_id to user_id and dropped app_user from database
        - old data had persisted and was not updated when project file was updated, so had to manually make the changes

## January 11th, 2022
* Added a project board to track todos
* In Progress:
    - Trying to route on-click event to open a video on our app
    - Navigation bar in progress
    - On-click event to add to user favorites
    - User profile for favorites, uploads, history
* Blockers: predominantly understanding React and TypeScript

## January 7th, 2022
* Making progress on entity and relationship mapping in jdl - refer to [progress file](jdl-entity-files/jdl-progress-log.md) for more details.
* Performed the following actions to run the application successfully with each change:
    ```
    1. Command Line: mvn clean install
    2. Save jdl file to root directory
    3. Command Line: jhipster import-jdl filename.jdl
    4. Command Line: ./mvnw
    5. Run localhost:8080 in browser
    6. Check to see if changes occurred
    ```
* Need to do a fresh install of jhipster (login issues for default accounts) and re-deploy to Heroku

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
    * Database: PostGresQL
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
