/*
	BPio.java - Interface for retrieving BPArea instances and managing
		protected areas.
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

import java.util.ArrayList;

public interface BPio {

	//synchronization
	static final Object areaLock = new Object();
	static final Object areaUserLock = new Object();
	static final Object areaGroupLock = new Object();
	static final Object areaLookupLock = new Object();

	public abstract boolean allowPlayer(BPArea area, Player p, BPArea.permissionLevel level);
	public abstract boolean allowPlayer(BPArea area, Player p, BPArea.permissionLevel level);
	public abstract boolean allowGroup(BPArea area, Group g, BPArea.permissionLevel level);
	public abstract boolean playerAllowed(int x, int y, int z, Player p, BPArea.permissionLevel level);
	public abstract boolean playerAllowed(BPArea area, Player p, BPArea.permissionLevel level);
	//checks to see if a player is at least permissionLevel level. 
	public abstract ArrayList<String> groupsAllowed(BPArea area, BPArea.permissionLevel minLevel);
	public abstract boolean protect(BPArea area);
	public abstract boolean unprotect(BPArea area);
	public abstract boolean delete(BPArea area);
	public abstract BPArea get(String name, Player p);
	public abstract BPArea get(int id);
	public abstract BPArea getLeading(int x, int y, int z);
	public abstract ArrayList<BPArea> getAll(int x, int y, int z);
	public abstract ArrayList<BPArea> getAll(Player p, BPArea.permissionLevel minLevel);
	public abstract boolean isAdminInAllAreas(BPArea area);
	public abstract Group getGroup(Sring groupName);
}