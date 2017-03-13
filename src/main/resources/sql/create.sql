CREATE TABLE message
(
  id VARCHAR PRIMARY KEY NOT NULL,
  header JSONB,
  subject VARCHAR,
  body VARCHAR,
  ins_date TIMESTAMP DEFAULT now()
);
CREATE TABLE message_source
(
  id VARCHAR NOT NULL,
  storage VARCHAR NOT NULL,
  path VARCHAR
);
CREATE UNIQUE INDEX message_source_id_uindex ON message_source (id);