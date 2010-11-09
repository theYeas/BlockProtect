CREATE TABLE bp_area (
	id INTEGER NOT NULL AUTO_INCREMENT,
	name Varchar(20),
	owner Varchar(20),
	priority INTEGER NOT NULL DEFAULT 0,
	x1 INTEGER NOT NULL DEFAULT 0,
	x2 INTEGER NOT NULL DEFAULT 0,
	y1 INTEGER NOT NULL DEFAULT 0,
	y2 INTEGER NOT NULL DEFAULT 0,
	z1 INTEGER NOT NULL DEFAULT 0,
	z2 INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY(id)
);
CREATE INDEX area_lookup ON bp_area(name, owner);
CREATE TABLE bp_protected (
	x INTEGER NOT NULL,
	y INTEGER NOT NULL,
	z INTEGER NOT NULL,
	bp_area_id INTEGER NOT NULL,
	CONSTRAINT pkey PRIMARY KEY(x,y,z,bp_area_id)
);
CREATE INDEX protected_areas ON bp_protected(bp_area_id);
CREATE TABLE bp_group (
	bp_area_id INTEGER NOT NULL,
	group_name VARCHAR(20),
	CONSTRAINT pkey PRIVATE KEY (bp_area_id, group_name)
);
CREATE TABLE bp_player (
	bp_area_id INTEGER NOT NULL,
	player_name VARCHAR(20),
	CONSTRAINT pkey PRIVATE KEY (bp_area_id, player_name)
);
