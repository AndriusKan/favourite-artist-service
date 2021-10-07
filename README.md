Favourite artist service API

Available endpoints:

GET http://localhost:8080/favourite-artist-service/search/{artist} - Finds, returns artists from Itunes service by
given artist string

POST http://localhost:8080/favourite-artist-service/save-favourite-artist - Consumes the body of the artist that was
returned by search, and saves it to the database for specific user

POST http://localhost:8080/favourite-artist-service/get-artist-albums - Consumes the body of the artist, and retrieves
artist album's, either from local database or makes a call to itunes

Application has swagger enabled - http://localhost:8080/swagger-ui/
