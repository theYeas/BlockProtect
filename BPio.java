import java.util.ArrayList;

public interface BPio {
	public abstract boolean allowPlayer(BPArea area, String playerName, permissionLevel level);
	public abstract boolean allowGroup(BPArea area, String groupName, permissionLevel level);
	public abstract boolean playerAllowed(BPArea area, String playerName, permissionLevel level);
	//checks to see if a player is at least permissionLevel level. 
	public abstract ArrayList<String> groupsAllowed(BPArea area, permissionLevel minLevel);
	public abstract boolean protect(BPArea area);
	public abstract boolean unprotect(BPArea area);
	public abstract boolean delete(BPArea area);
	public abstract BPArea get(String name, String owner);
	public abstract BPArea get(int id);
	public abstract BPArea getLeading(int x, int y, int z);
	public abstract ArrayList<BPArea> getAll(int x, int y, int z);
	public abstract ArrayList<BPArea> getAll(String playerName, permissionLevel minLevel);
	public abstract boolean hasUnownedInArea(BPArea area);
}