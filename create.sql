--
--	create.sql - basic tables for BPsql.java
--		
--		
--	BlockProtect - A hMod plugin to allow protection of areas.
--	Copyright (C) 2010  Matthew Wiese
--
--	This program is free software: you can redistribute it and/or modify
--	it under the terms of the GNU General Public License as published by
--	the Free Software Foundation, either version 3 of the License, or
--	(at your option) any later version.
--
--	This program is distributed in the hope that it will be useful,
--	but WITHOUT ANY WARRANTY; without even the implied warranty of
--	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--	GNU General Public License for more details.
--
--	You should have received a copy of the GNU General Public License
--	along with this program.  If not, see <http://www.gnu.org/licenses/>.
--

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
	permission_level INTEGER NOT NULL,
	CONSTRAINT pkey PRIVATE KEY (bp_area_id, group_name)
);
CREATE TABLE bp_player (
	bp_area_id INTEGER NOT NULL,
	player_name VARCHAR(20),
	permission_level INTEGER NOT NULL,
	CONSTRAINT pkey PRIVATE KEY (bp_area_id, player_name)
);
