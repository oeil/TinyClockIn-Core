CREATE TABLE User (
  id INTEGER NOT NULL ,
  email VARCHAR(255),
  token VARCHAR(255),

  CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE Action (
  id INTEGER NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  type INTEGER NOT NULL,
  description NVARCHAR(MAX),
  workstation INTEGER,
  userId INTEGER NOT NULL,

  CONSTRAINT pk_action PRIMARY KEY (id),
  CONSTRAINT fk_user FOREIGN KEY (userId) REFERENCES User(id)
);