/*
	BPsql.java - mySQL implementation of BPio.java. Handles all
		logic and optimizations.
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

public class BPsql implements BPio {
	public BPsql(String table_prefix) {
	
	}
	public boolean allowPlayer(BPArea area, String playerName, BPArea.permissionLevel level) {
	
	}
	public boolean allowGroup(BPArea area, String groupName, BPArea.permissionLevel level) {
	
	}
	public boolean playerAllowed(BPArea area, String playerName, BPArea.permissionLevel level) {
	
	}
	//checks to see if a player is at least permissionLevel level. 
	public ArrayList<String> groupsAllowed(BPArea area, BPArea.permissionLevel minLevel) {
	
	}
	public boolean protect(BPArea area) {
	
	}
	public boolean unprotect(BPArea area) {
	
	}
	public boolean delete(BPArea area) {
	
	}
	public BPArea get(String name, String owner) {
	
	}
	public BPArea get(int id) {
	
	}
	public BPArea getLeading(int x, int y, int z) {
	
	}
	public ArrayList<BPArea> getAll(int x, int y, int z) {
	
	}
	public ArrayList<BPArea> getAll(String playerName, BPArea.permissionLevel minLevel) {
	
	}
	public boolean isAdminInAllAreas(BPArea area) {
	
	}
	public boolean validGroupname(String groupName) {
	
	}
}