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
	public String protectToolName = "";
	public int protectToolId = 168;
	public String detectToolName = "Wooden Shovel";
	public int detectToolId = 169;
	public String unprotectAdminGroup = "Wooden Sword";
	public String allowAdminGroup = "";
	public String unallowAdminGroup = "";
	public String listAdminGroup = "";
	public String ownerAdminGroup = "";
	public String unprotectAdminGroup = "";
	public String ignoreAdminGroup = "";
	
	
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
	
	public boolean canModify(String playerName, Block b) {
		return canModify(playerName, b.getX(), b.getY(), b.getZ());
	}
	
	public boolean canModify(String playerName, int x, int y, int z) {
		//get most prevelant area
		//if owner is playerName
			// return true
		//else
			//get groups and players allowed
			//if player is in list of players
				//return true
			//else
				//get player and check groups
				//if in group
					//return true
		return false;
	}
	
	public boolean allowPlayer(int areaId, String playerName) {
		//on error return false
		//insert into bp_player (bp_area_id, name) values (areaId, "playerName") ON DUPLICATE IGNORE;
		return true;
	}
	
	public boolean disallowPlayer(int areaId, String playerName) {
		//on error return false
		//DROP FROM bp_player WHERE bp_area_id = areaId AND name = "playerName";
		return true;
	}
	
	public boolean allowGroup(int areaId, String groupName) {
		//on error return false
		//insert into bp_group (bp_area_id, group_id) values (areaId, "playerName") ON DUPLICATE IGNORE;
		return true;
	}
	
	public boolean disallowGroup(int areaId, String groupName) {
		//on error return false
		//DROP FROM bp_group WHERE bp_area_id = areaId AND name = "groupName";
		return true;
	}
	
	public boolean protect(String areaName, String ownerName, Block start, Block end) {
		//on error return false
		if (canProtectArea(ownerName, start, end) {
			//insert into bp_area (owner, name, x1, x2, y1, y2, z1, z2) VALUES ("ownerName", "areaName", start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ())
			//insert the f'ing mother load of points that are protected into bp_protected (x,y,z,bp_area_id)
			return true
		}
		return false;
	}
	
	public boolean canProtectArea(String ownerName, Block start, Block end) {
		//on error return false
		boolean flag = true;
		//select highest rated intersecting areas that dont intersect with eachother...
			//foreach area
				//check if user is the owner
					// !flag if ever false
		return flag;
	}
	
	public int getAreaId(String areaName, String ownerName) {
		int id = -1;
		//select id from bp_area where name = "areaName" and owner = "ownerName" LIMIT 1
		//if count > 0
			//id = ^^
		return id;
	}
	
	public boolean unprotect(int areaId) {
		//on error return false
		//drop from bp_area where id = areaId
		//drop from bp_protected where bp_area_id = affected_row()
		return true;
	}
	
	public boolean giveOwnership(int areaId, String newOwnerName) {
		//on error return false
		//update bp_area SET (owner = "newOwnerName") where id = areaId
		return true;
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
		/*	"/protect", " [areaName] <ownerName> - Allows an area to be marked and protected.");
			"/allow", " [areaName] -[p|g] [playerName|groupName] <ownerName> - Allows a player or group to modify an area.");
			"/disallow", " [areaName] -[p|g] [playerName|groupName] <ownerName> - Disallows a player or group to modify an area.");
			"/unprotect" , " [areaName] <ownerName> - optional ownerName if allowed to administrate. Unprotects area.
			"/listareas" , " <ownerName> - optional ownerName if allowed to administrate. Lists areas for area owner, or current player.
			"/giveownership" , " [areaName] [newOwnerName] <ownerName> - optional ownerName if allowed to administrate. Gives ownership of area to new owner.
		*/
			if (!player.canUseCommand(split[0])) {
				return false;
			}
			if (split[0].equalsIgnoreCase("/protect")) {
				String playerName = player.getName();
				int id = -1;
				if (split.length == 2) {
					String areaName = split[1];
					id = getAreaId(areaName, playerName);
					if (id == -1) {
						//drop from bp_protecting WHERE owner = playerName
						//insert into bp_protecting (name, owner) values (areaName, playerName)
						player.sendMessage(Colors.Rose + "Choose coordinates using "+toolName+".");
					} else {
						player.sendMessage(Colors.Rose + "Area name already exisits.");
					}
				} else {
					player.sendMessage(Colors.Rose + "Usage: /protect [areaName] - Allows an area to be marked and protected.");
				}
				return true;
			}
			if (split[0].equalsIgnoreCase("/unprotect")) {
				int id = 0;
				if (split.length >= 2) {
					String areaName = split[1];
					String ownerName = player.getName();
					if (split.length == 3 && (player.isAdmin() || (unprotectAdminGroup.length > 0 && player.isInGroup(unprotectAdminGroup))) && etc.getServer().matchPlayer(split[2]) != null)
						ownerName = split[2];
					id = getAreaId(areaName, ownerName);
				} else {
					id = -2;
					player.sendMessage(Colors.Rose + "Usage: /unprotect [areaName] <ownerName> - Unprotects an area. <ownerName> is for administrative use.");
				}
				if (id == -1) {
					player.sendMessage(Color.Rose + "Invalid area.");
				} else if (id > -1) {
					unprotect(id);
					player.sendMessage(Colors.Rose + "Area unprotected.");
				}
				return true;
			}
			if (split[0].equalsIgnoreCase("/allow")) {
				//allow", " [areaName] -[p|g] [playerName|groupName] - Allows a player or group to modify an area."
				int id = -1;
				String groupName = "";
				String playerName = "";
				if (split.length >= 4) { 
					String ownerName = player.getName();
					String areaName = split[1];
					if (split[2].equalsIgnoreCase("-g")) {
						//verify group and get group_id
						if () {
							groupName = split[3];
						} else {
							player.sendMessage(Colors.Rose + "Invalid Group.");
						}
					} else {
						if (etc.getServer().matchPlayer(split[3]).length > 0) {
							playerName = split[3];
						} else {
							player.sendMessage(Colors.Rose + "Invalid Player.");
						}
					}
					if (split.length == 5 && (player.isAdmin() || (unprotectAdminGroup.length > 0 && player.isInGroup(unprotectAdminGroup)) && etc.getServer().matchName(split[4]) != null)
						ownerName = split[4];
					id = getAreaId(areaName, ownerName);
				} else {
					player.sendMessage(Colors.Rose + "Usage: /allow [areaName] -[p|g] [playerName|groupName] <ownerName> Allows a user or group to modify an area.");
				}
				if (id == -1) {
					player.sendMessage(Colors.Rose + "Invalid Area.");
				} else if (id > -1 && group.length > 0) {
					allowGroup(id, groupName);
					player.sendMessage(Colors.Rose + "Group allowed.");
				} else if (id > -1 && playerName.length > 0) {
					allowPlayer(id, playerName);
					player.sendMessage(Colors.Rose + "Player allowed.");
				}
				return true;
			}
			if (split[0].equalsIgnoreCase("/unallow")) {
			int id = -1;
				String groupName = "";
				String playerName = "";
				if (split.length >= 4) { 
					String ownerName = player.getName();
					String areaName = split[1];
					if (split[2].equalsIgnoreCase("-g")) {
						//verify group and get group_id
						if () {
							groupName = split[3];
						} else {
							player.sendMessage(Colors.Rose + "Invalid Group.");
						}
					} else {
						if (etc.getServer().matchPlayer(split[3]).length > 0) {
							playerName = split[3];
						} else {
							player.sendMessage(Colors.Rose + "Invalid Player.");
						}
					}
					if (split.length == 5 && (player.isAdmin() || (unprotectAdminGroup.length > 0 && player.isInGroup(unprotectAdminGroup)) && etc.getServer().matchName(split[4]) != null)
						ownerName = split[4];
					id = getAreaId(areaName, ownerName);
				} else {
					player.sendMessage(Colors.Rose + "Usage: /unallow [areaName] -[p|g] [playerName|groupName] <ownerName> Allows a user or group to modify an area.");
				}
				if (id == -1) {
					player.sendMessage(Colors.Rose + "Invalid Area.");
				} else if (id > -1 && group.length > 0) {
					unallowGroup(id, groupName);
					player.sendMessage(Colors.Rose + "Group unallowed.");
				} else if (id > -1 && playerName.length > 0) {
					unallowPlayer(id, playerName);
					player.sendMessage(Colors.Rose + "Player unallowed.");
				}
				return true;
			}
			if (split[0].equalsIgnoreCase("/listareas")) {
			
			}
			if (split[0].equalsIgnoreCase("/giveownership")) {
			
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