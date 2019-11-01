USE moviehatbot ;

CREATE TABLE FavLists (
  id           INT NOT NULL AUTO_INCREMENT,
  name         VARCHAR(255) NOT NULL,
  date         TIMESTAMP  NOT NULL,
  createUserId INT NOT NULL,
  PRIMARY KEY (ID)
);