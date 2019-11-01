USE moviehatbot ;

CREATE TABLE Users (
  id        INT NOT NULL AUTO_INCREMENT,
  telUserId DECIMAL(15) NOT NULL,
  alias     VARCHAR(255),
  language  VARCHAR(5),
  adult     BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (ID)
);