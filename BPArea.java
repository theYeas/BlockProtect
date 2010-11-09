public class BPArea {
	public String name = "";
	public String owner = "";
	public BoundingBox b = null;
	public int priority = 0;
	
	public BPArea(String name, String owner) {
		this.name = name;
		this.owner = owner;
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
		this.b.setLower(start);
	}
	public void setEnd(int x, int y, int z) {
		setEnd(new Point3d(x,y,z));
	}
	public void setEnd(Point3d end) {
		this.b.setUpper(end);
	}
}