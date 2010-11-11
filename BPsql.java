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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class BPsql implements BPio {
	String table_prefix = "";
	
	public BPsql(String table_prefix) {
		this.table_prefix = table_prefix;
	}
	public boolean allowPlayer(BPArea area, Player p, BPArea.permissionLevel level) {
		synchronized(areaUserLock) {
			try {
				Connection conn = getConn();
				PreparedStatement st = conn.createPreparedStatement("INSERT INTO "+table_prefix+"area_user (area_id, user_id, level) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE level = VALUES(level)");
				st.setInt(1, area.getId());
				st.setInt(2, p.getSqlId());
				st.setInt(3, level.ordinal());
				st.executeUpdate();
				st.close();
				conn.close();
			} catch (SQLException e) (
				BlockProtectPlugin.log.log("allowPlayer() SQL Error", level.SEVERE, e);
			} finally {
				try {
					if (st != null) {
						st.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException ex) {}
			}
		}
	}
	public boolean allowGroup(BPArea area, Group g, BPArea.permissionLevel level) {
		synchronized(areaGroupLock) {
			try {
				Connection conn = getConn();
				PreparedStatement st = conn.createPreparedStatement("INSERT INTO "+table_prefix+"area_group (area_id, group_id, level) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE level = VALUES(level)");
				st.setInt(1, area.getId());
				st.setInt(2, g.ID);
				st.setInt(3, level.ordinal());
				st.executeUpdate();
				st.close();
				conn.close();
			} catch (SQLException e) (
				BlockProtectPlugin.log.log("allowGroup() SQL Error", level.SEVERE, e);
			} finally {
				try {
					if (st != null) {
						st.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException ex) {}
			}
		}
	}
	public boolean playerAllowed(BPArea area, Player p, BPArea.permissionLevel level) {
		if (area.
		synchronized(areaUserGroup) {
			//hey0 never added a lock for users... this is just in case
			//synchronized(DataSource.userLock) {
				try {
					Connection conn = getConn();
					PreparedStatement st = conn.createPreparedStatement("SELCT user_id, group_id FROM ");
					//select count(au.user_id), ag.group_id FROM area AS a 
					//damn groups are stored as : group1,group2,group3 ... why~!!~~
					st.setInt(1, area.getId());
					st.setInt(2, level.ordinal());
					st.setInt(3, p.getSqlId());
					ResultSet rs = st.executeQuery();
				} catch (SQLException e) (
					BlockProtectPlugin.log.log("playerAllowed() SQL Error", level.SEVERE, e);
				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
						if (st != null) {
							st.close();
						}
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException ex) {}
				}
			}
			//}
		}
	}
	public abstract boolean playerAllowed(int x, int y, int z, Player p, BPArea.permissionLevel level) {
	
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
	public BPArea get(String name, Player p) {
	
	}
	public BPArea get(int id) {
	
	}
	public BPArea getLeading(int x, int y, int z) {
	
	}
	public ArrayList<BPArea> getAll(int x, int y, int z) {
	
	}
	public ArrayList<BPArea> getAll(Player p, BPArea.permissionLevel minLevel) {
	
	}
	public boolean isAdminInAllAreas(BPArea area) {
	
	}
	public Group getGroup(String groupName) {
	
	}
}