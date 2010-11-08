import java.util.logging.Logger;

/**
*
* @author theYeas
*/
public class BlockProtectPlugin extends Plugin {
	private Listener l = new Listener(this);
	protected static final Logger log = Logger.getLogger("Minecraft");
	private String name = "BlockProtect";
	private String version = "0.1";

	public void enable() {
		log.info(name + " " + version + " Plugin Enabled.");
		etc.getInstance().addCommand("/protect", " [areaName] - Allows an area to be marked and protected.");
		etc.getInstance().addCommand("/allow", " [areaName] -[p|g] [playerName|groupName] - Allows a player or group to modify an area.");
		etc.getInstance().addCommand("/disallow", " [areaName] -[p|g] [playerName|groupName] - Disallows a player or group to modify an area.");
	}

	public void disable() {
		log.info(name + " " + version + " Plugin Disabled.");
		etc.getInstance().addCommand("/protect");
		etc.getInstance().addCommand("/allow");
		etc.getInstance().addCommand("/disallow");
	}

	public void initialize() {
		BlockProtectListener l = new BlockProtectListener();
		etc.getLoader().addListener(PluginLoader.Hook.CHAT, l, this, PluginListener.Priority.HIGH);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_CREATED, l, this, PluginListener.Priority.HIGH);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_DESTROYED, l, this, PluginListener.Priority.HIGH);
		log.info(name + " " + version + " initialized");
	}
	
	public boolean canModify(String playerName, ComplexBlock b) {
		return canModify(playerName, b.getX(), b.getY(), b.getZ());
	}
	
	public boolean canModify(String playerName, Block b) {
		return canModify(playerName, b.getX(), b.getY(), b.getZ());
	}
	
	public boolean canModify(String playerName, int x, int y, int z) {
		//check db
		
		return false;
	}
	
	public boolean allowPlayer(String playerName, String ownerName, String areaName) {
		//check db
		
		return false;
	}
	
	public boolean disallowPlayer(String playerName, String ownerName, String areaName) {
		//check db
		
		return false;
	}
	
	public boolean allowGroup(String groupName, String ownerName, String areaName) {
		//check db
		
		return false;
	}
	
	public boolean disallowGroup(String groupName, String ownerName, String areaName) {
		//check db
		
		return false;
	}
	
	public boolean protect(String areaName, String ownerName, Block start, Block end) {
		if (canProtectArea(ownerName, start, end) {
			
		}
		return false;
	}
	
	public boolean canProtectArea(String ownerName, Block start, Block end) {
	///Checks to see if an area can be protected
		boolean flag = true;
		//select highest rated intersecting areas that dont intersect with eachother...
			//foreach area
				//check if user is the owner
					// !flag if ever false
		return flag;
	}
	
	public boolean unprotect(String areaName, String ownerName) {
	
	}
	
	public boolean giveOwnership(String areaName, String ownerName, String newOwnerName) {
	
	}
	
	private void loadProperties() {
		File f = new File(this.name+".properties");
		if (!f.exists()) {
			//make properties
			log.info(name + ": Writing Blank Properties File");
			PropertiesFile props = new PropertiesFile(this.name + ".properties");
			try {
				//write defaults
			} catch (Exception e) {
				log.log(Level.SEVERE, "Exception while reading from server.properties", e);
			}
			props.save();
			//save to file
		} else {
			//load properties
			PropertiesFile props = new PropertiesFile(this.name + ".properties");
			try {
				//read file
			} catch (Exception e) {
				log.log(Level.SEVERE, "Exception while reading from server.properties", e);
			}
			props.load();
		}
	}

	public class BlockProtectListener extends PluginListener {
		BlockProtectPlugin p;

		// This controls the accessability of functions / variables from the main class.
		public Listener(BlockProtectPlugin plugin) {
			p = plugin;
		}
		
		public boolean onCommand(Player player, String[] split) {
			//commands: protect, unprotect, allow, disallow, listareas, giveownership
			String playerName = player.getName();
			if (split[0].equals("/protect")) {
			
			}
			if (split[0].equals("/unprotect")) {
			
			}
			if (split[0].equals("/allow")) {
			
			}
			if (split[0].equals("/unallow")) {
			
			}
			if (split[0].equals("/listareas")) {
			
			}
			if (split[0].equals("/giveownership")) {
			
			}
			return false;
		}

		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand) {	
			return canModify(player.getName(), blockPlaced);
		}
		
		public boolean onBlockDestroy(Player player, Block block) {
			return canModify(player.getName(), block);
		}
	}
}