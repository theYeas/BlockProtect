/*
	BPArea.java - Standard protectable area.
*/
/*-
	BlockProtect - A hMod plugin to allow protection of areas.
	Copyright (C) 2010  Matthew Wiese

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


import javax.media.j3d.BoundingBox;
import javax.media.j3d.Point3d;

public class BPArea {
	public enum permissionLevel {
		NONE, ENTER, CREATE, DESTROY, CREATE_DESTROY, ADMINISTRATE
		//NONE<ENTER<CREATE, DESTROY<CREATE_DESTROY<ADMINISTRATE
	}
	private int id = -1;
	private String name = "";
	private int owner = -1;
	private BoundingBox b = null;
	//private int priority = 0; not in use yet
	private permissionLevel level = permissionLevel.NONE;
	
	public BPArea(String name, int owner, permissionLevel level, int id, int x1, int y1, int z1, int x2, int y2, int z2) {
		this.name = name;
		this.owner = owner;
		this.level = level;
		this.id = id;
		this.b = new BoundingBox();
		this.b.setLower(new Point3d(x1,y1,z1));
		this.b.setUpper(new Point3d(x2,y2,z2));
	}
	
	public BPArea(String name, int owner, permissionLevel level) {
		this.name = name;
		this.owner = owner;
		this.level = level;
	}
	public BPArea(String name, int owner, int id, permissionLevel level, Point3d start, Point3d end) {
		this.id = id;
		this.level = level;
		this.name = name;
		this.owner = owner;
		b = new BoundingBox(start,end);
	}
	public permissionLevel getLevel() {
		return level;
	}
	public void setLevel(permissionLevel level) {
		this.level = level;
	}
	public boolean isStarted() {
		return b != null;
	}
	public boolean isOwner(Player p) {
		return this.getOwner().equals(p.getSqlId());
	}
	public boolean intersects(BPArea to) {
		if (b != null && to.b != null)
			return b.intersect(to.b);
		return true;
	}
	public int getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public String getOwner() {
		return this.owner;
	}
	public Point3d getStart() {
		return this.b.getLower();
	}
	public Point3d getEnd() {
		return this.b.getUpper();
	}
	public int getPriority() {
		return this.priority;
	}
	public int getId() {
		return this.id;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setPriority(int p) {
		this.priority = p;
	}
	public void setStart(int x, int y, int z) {
		setStart(new Point3d(x,y,z));
	}
	public void setStart(Point3d start) {
		if (this.b == null) {
			this.b = new BoundingBox(start, new Point3d(0,0,0));
		} else {
			this.b.setLower(start);
		}
	}
	public void setEnd(int x, int y, int z) {
		setEnd(new Point3d(x,y,z));
	}
	public void setEnd(Point3d end) {
		this.b.setUpper(end);
	}
}