USE moviehatbot ;

CREATE TABLE UsersToFavLists (
  id          INT NOT NULL AUTO_INCREMENT,
  userId      INT NOT NULL,
  favListId   INT NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT user_fk FOREIGN KEY (userId) REFERENCES moviehatbot.Users (id),
  CONSTRAINT movie_fk FOREIGN KEY (favListId) REFERENCES moviehatbot.FavLists (id)
);