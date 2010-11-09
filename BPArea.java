import javax.vecmath.Point3d;
import javax.vecmath.BoundingBox;
import java.util.ArrayList;

public abstract class BPArea {
	public String name = "";
	public String owner = "";
	public BoundingBox b = null;
	public int priority = 0;
	public boolean finished = false;
	
	public BPArea(String name, String owner) {
		this.name = name;
		this.owner = owner;
	}
	
	public abstract void save() {
		
	}
	
	public abstract void load(int id) {
		
	}
	
	public abstract void load(String name, String owner) {
		
	}
	
	public boolean isOwner(String owner) {
		return this.owner.equals(owner);
	}
	
	public boolean intersects(BPArea to) {
		if (b != null && to.b != null)
			return b.intersect(to.b);
		return true;
	}
	
	public abstract boolean allowPlayer(String playerName) {
		//on error return false
		//insert into bp_player (bp_area_id, name) values (areaId, "playerName") ON DUPLICATE IGNORE;
		
		return true;
	}
	
	public abstract boolean disallowPlayer(String playerName) {
		//on error return false
		//DROP FROM bp_player WHERE bp_area_id = areaId AND name = "playerName";
		return true;	
	}
	
	public abstract boolean allowGroup(String groupName) {
		//on error return false
		//insert into bp_group (bp_area_id, group_id) values (areaId, "playerName") ON DUPLICATE IGNORE;
		return true;
	}
	
	public abstract boolean disallowGroup(String groupName) {
		//on error return false
		//DROP FROM bp_group WHERE bp_area_id = areaId AND name = "groupName";
		return true;
	}
	
	public abstract boolean playerAllowed(String playerName) {
	
	}
	
	public abstract ArrayList<String> groupsAllowed(String playerName) {
		
	}
	
	public abstract boolean protect() {
		//insert into bp_area (owner, name, x1, x2, y1, y2, z1, z2) VALUES ("ownerName", "areaName", start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ())
		//insert the f'ing mother load of points that are protected into bp_protected (x,y,z,bp_area_id)
	}
	
	public abstract boolean unprotect() {
	
	}
	
	public abstract static boolean delete(BPArea area) {
	
	}
	
	public abstract static int contains(String name, String owner) {
		int id = -1;
		//select id from bp_area where name = "areaName" and owner = "ownerName" LIMIT 1
		//if count > 0
			//id = ^^
		return id;
	}
	
	public abstract static BPArea getLeading(int x, int y, int z) {
		
	}
	
	public abstract static ArrayList<BPArea> getAll(int x, int y, int z) {
	
	}
	
	public abstract static ArrayList<BPArea> getAll(String owner) {
	
	}
}