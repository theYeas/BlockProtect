import java.util.ArrayList;

public interface BPio {
	public abstract boolean allowPlayer(BPArea area, String playerName);
	public abstract boolean disallowPlayer(BPArea area, String playerName);
	public abstract boolean allowGroup(BPArea area, String groupName);
	public abstract boolean disallowGroup(BPArea area, String groupName);
	public abstract boolean playerAllowed(BPArea area, String playerName);
	public abstract ArrayList<String> groupsAllowed(BPArea area, String playerName);
	public abstract boolean protect(BPArea area);
	public abstract boolean unprotect(BPArea area);
	public abstract boolean delete(BPArea area);
	public abstract BPArea get(String name, String owner);
	public abstract BPArea get(int id);
	public abstract BPArea getLeading(int x, int y, int z);
	public abstract ArrayList<BPArea> getAll(int x, int y, int z);
	public abstract ArrayList<BPArea> getAll(String owner);
}