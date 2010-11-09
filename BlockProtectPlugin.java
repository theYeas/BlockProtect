import java.util.logging.Logger;
import java.sql.*;
import java.io.File;
/**
*
* @author theYeas
*/
public class BlockProtectPlugin extends Plugin {

	private class BPTempArea {
		public String name;
		public String owner;
		public int x1;
		public int y1;
		public int z1;
		
		public BPTempArea(String name, String owner) {
			this.name = name;
			this.owner = owner;
			x1 = 0;
			y1 = 0;
			z1 = 0;
		}
	}
	private Listener l = new Listener(this);
	protected static final Logger log = Logger.getLogger("Minecraft");
	private String name = "BlockProtect";
	private String version = "0.1";
	public String protectToolName = "Wooden Shovel";
	public int protectToolId = 168;
	public String detectToolName = "Wooden Sword";
	public int detectToolId = 169;
	public String allowAdminGroup = "";
	public String unallowAdminGroup = "";
	public String listAdminGroup = "";
	public String ownerAdminGroup = "";
	public String unprotectAdminGroup = "";
	public String ignoreAdminGroup = "";
	public String sqlTablePrefix = "bp_";
	private _srvpropsChecked = false;
	private _configFolder = "";
	private Connection conn;
	
	priavte HashMap<String, BPTempArea> protecting = null;
	
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
		CheckServerConfig();
		loadProperties();
		protecting = new HashMap<String, BPTempArea>();
		BlockProtectListener l = new BlockProtectListener();
		etc.getLoader().addListener(PluginLoader.Hook.CHAT, l, this, PluginListener.Priority.HIGH);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_CREATED, l, this, PluginListener.Priority.HIGH);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_DESTROYED, l, this, PluginListener.Priority.HIGH);
		conn = etc.getSQLConnection();
		log.info(name + " " + version + " initialized");
	}
	
	/**
     * I don't know about you, but I don't like having so many config
     * config files in the bin folder, so I decided to throw this in
     * in case you want to place this plugin's config somewhere else.
	 * ---juntalis
     */
    private void CheckServerConfig() {
        if(_srvpropsChecked) return;

        PropertiesFile properties = new PropertiesFile("server.properties");
        properties.load();
        try {
            if(properties.keyExists("pluginconfig"))
                _configFolder = properties.getString("pluginconfig");
            _srvpropsChecked = true;
        } catch(Exception e) {
            log.log(Level.SEVERE, "Exception while reading from server.properties", e);
        }
        properties = null;

        // Next, check to make sure we don't have to create aforementioned
        if(_configFolder != null) {
            File dirCheck = new File(System.getProperty("user.dir") + File.separator + _configFolder);
            if(!dirCheck.exists()) {
                if(!dirCheck.mkdir()) {
                    log.log(Level.SEVERE, "Could not create plugin configuration folder. Defaulting to root folder.");
                    _configFolder = "";
                } else {
                    log.info("Plugin configuration folder successfully created.");
                }
            } else {
                if(!dirCheck.isDirectory()) {
                    log.log(Level.SEVERE, "File exists with the name of specified pluginconfig. Defaulting to root folder.");
                    _configFolder = "";
                }
            }
            dirCheck = null;
        }
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
		String propPath = _configFolder + this.name + ".properties";
		File f = new File(propPath);
		if (!f.exists()) {
			//make properties
			log.info(this.name + ": Writing Blank Properties File");
			PropertiesFile props = new PropertiesFile(propPath);
			try {
				//write defaults
				props.setString("protectToolName", "Wooden Shovel");
				props.setInt("protectToolId", 168);
				props.setString("detectToolName", "Wooden Sword");
				props.setInt("detectToolId", 169);
				props.setString("allowAdminGroup", "");
				props.setString("unallowAdminGroup", "");
				props.setString("listAdminGroup", "");
				props.setString("ownerAdminGroup", "");
				props.setString("unprotectAdminGroup", "");
				props.setString("ignoreAdminGroup", "");	
				props.save();
			} catch (Exception e) {
				log.log(Level.SEVERE, "Exception while writing to "+propPath, e);
			}
			props.save();
			//save to file
		} else {
			//load properties
			PropertiesFile props = new PropertiesFile(propPath);
			try {
				//read file		
				props.load();
				protectToolName = props.getString("protectToolName");
				protectToolId = props.getInt("protectToolId");
				detectToolName = props.getString("detectToolName");
				detectToolId = props.getInt("detectToolId");
				allowAdminGroup = props.getString("allowAdminGroup");
				unallowAdminGroup = props.getString("unallowAdminGroup");
				listAdminGroup = props.getString("listAdminGroup");
				ownerAdminGroup = props.getString("ownerAdminGroup");
				unprotectAdminGroup = props.getString("unprotectAdminGroup");
				ignoreAdminGroup = props.getString("ignoreAdminGroup");
			} catch (Exception e) {
				log.log(Level.SEVERE, "Exception while reading from "+propPath, e);
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
						protecting.remove(playerName);
						BPTempArea t = new BPTempArea(areaName, playerName);
						protecting.put(playerName, t);
						player.sendMessage(Colors.Rose + "Choose coordinates by right clicking blocks with a "+toolName+".");
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
				String ownerName = player.getName();
				if (split.length == 2 && (player.isAdmin() || (listAdminGroup.length > 0 && player.isInGroup(listAdminGroup)) && etc.getServer().matchName(split[1]) != null)
					ownerName = split[1];
				//select owner, name from bp_area where owner = ownerName
				//for each
					//player.sendMessage(Colors.Rose +"");
				//else
					//player.sendMessage(Colors.Rose +"Player does not have any protected areas.");
				return true;
			}
			if (split[0].equalsIgnoreCase("/giveownership")) {
				//"/giveownership" , " [areaName] [newOwnerName] <ownerName> - optional ownerName if allowed to administrate. Gives ownership of area to new owner.
				String ownerName = player.getName();
				String areaName = "";
				String newOwner = "";
				if (split.length >= 3) {
					areaName = split[1];
					if (etc.getServer().matchName(split[2])) {
						newOwner = split[2];
					} else {
						player.sendMessage(Colors.Rose+"New owner cannot be found.");
					}
					if (split.length == 2 && (player.isAdmin() || (listAdminGroup.length > 0 && player.isInGroup(listAdminGroup)) && etc.getServer().matchName(split[3]) != null)
						ownerName = split[3];
					int id = getAreaId(areaName, ownerName);
					if (id == -1) {
						player.sendMessage(Colors.Rose+"Cannot find area.");
					} else {
						giveOwnership(id, newOwner);
					}
				} else {
					player.sendMessage(Colors.Rose+"Usage: /giveownership [areaName] [newOwnerName] <ownerName> - Gives ownership of area to a new owner. <ownerName> is for admin purposes.");
				}
				return true;
			}
			return false;
		}

		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand) {	
			if (itemInHand==protectToolId && player.canUseCommand("/protect") && protecting.contains(player.getName())) {
				BPTempArea t = protecting.get(player.getName());
				if (t.x1 + t.y1 + t.z1 >= 0) {
					t.x1 = blockClicked.getX();
					t.y1 = blockClicked.getY();
					t.z1 = blockClicked.getZ();
					player.sendMessage(Colors.Rose+"First coordinate selected.");
				} else {
					protect(t.name, t.owner, t.x1, t.y1, t.z1, blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
					player.sendMessage(Colors.Rose+"Second coordinate selected, now protecting area.");
					protecting.remove(player.getName());
				}
				return true;
			} else if (itemInHand==detectToolId) {
				//SELECT owner, name FROM bp_area INNER JOIN bp_protected on bp_area.id = bp_protected.bp_area_id ORDER BY priority DESC, area.id DESC
				//for each row found
					//player.sendMessage(Colors.Rose+"");
				//else
					//player.sendMessage(Colors.Rose+"There are not any protected areas here.");
				return true;
			} else if ( (itemInHand>267 && itemInHand<280) || itemInHand==256 || itemInHand==257 || itemInHand==258 || itemInHand==290 || itemInHand==291|| itemInHand==292|| itemInHand==293 || itemInHand==294 ) {
				return false;
			}
			return !(player.isInGroup(ignoreAdminGroup) || canModify(player.getName(), blockPlaced));
		}
		
		public boolean onBlockDestroy(Player player, Block block) {
			return !(player.isInGroup(ignoreAdminGroup) || canModify(player.getName(), block));
		}
	}
}