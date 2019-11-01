USE moviehatbot ;

CREATE TABLE FavMovies (
  id          INT NOT NULL AUTO_INCREMENT,
  name        VARCHAR(255),
  tmdbId      INT,
  favListId   INT NOT NULL,
  addDate     TIMESTAMP NOT NULL,
  addUserId   INT NOT NULL,
  watched     BOOLEAN DEFAULT FALSE,

  PRIMARY KEY (id),
  CONSTRAINT favMovies_user_fk FOREIGN KEY (addUserId) REFERENCES moviehatbot.Users (id),
  CONSTRAINT favMovies_list_fk FOREIGN KEY (favListId) REFERENCES moviehatbot.FavLists (id)
);
