public class BPAreaSQL extends BPAreaBase implements BPArea {
public boolean allowPlayer(String playerName);
		//on error return false
		//insert into bp_player (bp_area_id, name) values (areaId, "playerName") ON DUPLICATE IGNORE;
	public boolean disallowPlayer(String playerName);
		//on error return false
		//DROP FROM bp_player WHERE bp_area_id = areaId AND name = "playerName";
	public boolean allowGroup(String groupName);
		//on error return false
		//insert into bp_group (bp_area_id, group_id) values (areaId, "playerName") ON DUPLICATE IGNORE;
	public boolean disallowGroup(String groupName);
		//on error return false
		//DROP FROM bp_group WHERE bp_area_id = areaId AND name = "groupName";
	public boolean playerAllowed(String playerName);
	public ArrayList<String> groupsAllowed(String playerName);
	public boolean protect();
		//insert into bp_area (owner, name, x1, x2, y1, y2, z1, z2) VALUES ("ownerName", "areaName", start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ())
		//insert the f'ing mother load of points that are protected into bp_protected (x,y,z,bp_area_id)
	public boolean unprotect();
	public static boolean delete(BPArea area);
	public static int contains(String name, String owner);
		//int id = -1;
		//select id from bp_area where name = "areaName" and owner = "ownerName" LIMIT 1
		//if count > 0
			//id = ^^
		//return id;
	public static BPArea getLeading(int x, int y, int z);
	public static ArrayList<BPArea> getAll(int x, int y, int z);
	public static ArrayList<BPArea> getAll(String owner);
}