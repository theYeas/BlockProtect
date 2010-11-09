import javax.vecmath.Point3d;
import javax.vecmath.BoundingBox;
import java.util.ArrayList;

public interface BPArea {
	public boolean isOwner(String playerName);
	public boolean isComplete();
	public String getName();
	public String getOwner();
	public Point3d getStart();
	public Point3d getEnd();
	public int getPriority();
	public void setOwner(String owner);
	public void setPriority(int p);
	public void setStart(int x, int y, int z);
	public void setStart(Point3d start);
	public void setEnd(int x, int y, int z);
	public void setEnd(Point3d end);
	public void save();
	public void load(int id);
	public void load(String name, String owner);
	public boolean allowPlayer(String playerName);
	public boolean disallowPlayer(String playerName);
	public boolean allowGroup(String groupName);
	public boolean disallowGroup(String groupName);
	public boolean playerAllowed(String playerName);
	public ArrayList<String> groupsAllowed(String playerName);
	public boolean protect();
	public boolean unprotect();
	public static boolean delete(BPArea area);
	public static int contains(String name, String owner);
	public static BPArea getLeading(int x, int y, int z);
	public static ArrayList<BPArea> getAll(int x, int y, int z);
	public static ArrayList<BPArea> getAll(String owner);
}