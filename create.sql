CREATE TABLE bp_area (
	id Integer NOT NULL AUTO_INCREMENT,
	name Varchar(20),
	owner Varchar(20),
	priority Integer NOT NULL DEFAULT 0,
	x1 Integer NOT NULL DEFAULT 0,
	x2 Integer NOT NULL DEFAULT 0,
	y1 Integer NOT NULL DEFAULT 0,
	y2 Integer NOT NULL DEFAULT 0,
	z1 Integer NOT NULL DEFAULT 0,
	z2 Integer NOT NULL DEFAULT 0,
	PRIMARY KEY(id)
);
CREATE INDEX area_lookup ON bp_area(name, owner);
CREATE TABLE bp_protected (
	x Integer NOT NULL,
	y Integer NOT NULL,
	z Integer NOT NULL,
	bp_area_id Integer NOT NULL,
	CONSTRAINT pkey PRIMARY KEY(x,y,z,bp_area_id)
);
CREATE INDEX protected_areas ON bp_protected(bp_area_id);