import java.util.ArrayList;

public interface BPio {
	public abstract boolean allowPlayer(String playerName);
	public abstract boolean disallowPlayer(String playerName);
	public abstract boolean allowGroup(String groupName);
	public abstract boolean disallowGroup(String groupName);
	public abstract boolean playerAllowed(String playerName);
	public abstract ArrayList<String> groupsAllowed(String playerName);
	public abstract boolean protect();
	public abstract boolean unprotect();
	public abstract boolean delete(BPArea area);
	public abstract int contains(String name, String owner);
	public abstract BPArea get(int id);
	public abstract BPArea getLeading(int x, int y, int z);
	public abstract ArrayList<BPArea> getAll(int x, int y, int z);
	public abstract ArrayList<BPArea> getAll(String owner);
}