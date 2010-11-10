public class BPArea {
	public enum permissionLevel {
		NONE, ENTER, CREATE, DESTROY, CREATE_DESTROY, ADMINISTRATE
		//NONE<ENTER<CREATE, DESTROY<CREATE_DESTROY<ADMINISTRATE
	}
	private int id = -1;
	private String name = "";
	private String owner = "";
	private BoundingBox b = null;
	private int priority = 0;
	private permissionLevel level = permissionLevel.NONE;
	
	public BPArea(String name, String owner, permissionLevel level, int id, int x1, int y1, int z1, int x2, int y2, int z2) {
		this.name = name;
		this.owner = owner;
		this.level = level;
		this.id = id;
		this.b = new BoundingBox();
		this.b.setLower(new Point3d(x1,y1,z1));
		this.b.setUpper(new Point3d(x2,y2,z2));
	}
	
	public BPArea(String name, String owner, permissionLevel level) {
		this.name = name;
		this.owner = owner;
		this.level = level;
	}
	public BPArea(String name, String owner, int id, int p, Point3d start, Point3d end) {
		this.id = id;
		this.p = p;
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
	public boolean isOwner(String owner) {
		return this.getOwner().equals(owner);
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