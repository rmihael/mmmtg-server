# --- !Ups

INSERT INTO blocks VALUES(1, 'Revised', 'REV');
INSERT INTO blocks VALUES(2, 'Alliances', 'AI');

INSERT INTO cards(name, block_id) VALUES ('Force of Will', 1);
INSERT INTO cards(name, block_id) VALUES ('Underground Sea', 2);
INSERT INTO cards(name, block_id) VALUES ('Tropical Island', 2);

# --- !Downs

DELETE FROM blocks WHERE id IN (1, 2);
DELETE FROM cards WHERE id IN (1, 2, 3);