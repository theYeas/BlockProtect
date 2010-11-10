import java.util.logging.Logger;
import java.sql.*;
import java.io.File;
/**
*
* @author theYeas
*/
public class BlockProtectPlugin extends Plugin {
	private Listener l = new Listener(this);
	protected static final Logger log = Logger.getLogger("Minecraft");
	private String name = "BlockProtect";
	private String version = "0.1";
	public String protectToolName = "Wooden Shovel";
	public int protectToolId = 168;
	public String detectToolName = "Wooden Sword";
	public int detectToolId = 169;
	public String allowAdminGroup = "";
	public String listAdminGroup = "";
	public String ownerAdminGroup = "";
	public String unprotectAdminGroup = "";
	public String ignoreAdminGroup = "";
	public String detectGroup = "";
	public String sqlTablePrefix = "bp_";
	public String dataFolder = File.separator+"bp";
	public boolean useSQL = true;
	public int defaultPermissionLevel = BPArea.permissionLevels.NONE;
	private _srvpropsChecked = false;
	private _configFolder = "";
	BPio io = null;
	
	priavte HashMap<String, BPArea> selecting = null;
	
	public void enable() {
		log.info(name + " " + version + " Plugin Enabled.");
		etc.getInstance().addCommand("/protect", " [areaName] - Allows an area to be marked and protected.");
		etc.getInstance().addCommand("/allow", " [areaName] -[p|g] [playerName|groupName] [e/c/d/a] - Allows a player or group to modify an area. [e/c/d/a] Create Destroy Administrate");
		etc.getInstance().addCommand("/listareas", " [e|c|d|b|a] (playerName) - Lists areas that meet given permission level. (optional) playerName for administrators.");
	}

	public void disable() {
		log.info(name + " " + version + " Plugin Disabled.");
		etc.getInstance().addCommand("/protect");
		etc.getInstance().addCommand("/allow");
		etc.getInstance().addCommand("/listareas");
	}

	public void initialize() {
		CheckServerConfig();
		loadProperties();
		if (useSQL) {
			io = new BPsql(sqlTablePrefix);
		} else {
			io = new BPfile(dataFolder);
		}
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
	
	public boolean canModify(Player p, Block b) {
		return canModify(p, b.getX(), b.getY(), b.getZ());
	}
	
	public boolean canModify(Player p, int x, int y, int z, BPArea.permissionLevel level) {
		BPArea area = BPArea.getLeading(x,y,z);
		if (area.isOwner(p.getName())) {
			return true;
		} else {
			if (io.playerAllowed(area, p.getName(), level) {
				return true;
			} else {
				ArrayList<String> allowedGroups = io.groupsAllowed(area, level);
				for(String s : allowedGroups) {
					if (p.isInGroup(s))
						return true;
				}
			}
		}
		return false;
	}
	
	public boolean canProtectArea(BPArea area) {
		boolean flag = true;
		//select highest rated intersecting areas that dont intersect with eachother...
			//foreach area
				//check if user is the owner
					// !flag if ever false
		return flag;
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
				props.getString("sqlTablePrefix", "bp_");
				props.getString("dataFolder", "/bp");
				props.setBoolean("useSQL", true);
				props.setString("protectToolName", "Wooden Shovel");
				props.setInt("protectToolId", 168);
				props.setString("detectToolName", "Wooden Sword");
				props.setInt("detectToolId", 169);
				props.setString("allowAdminGroup", "");
				props.setString("listAdminGroup", "");
				props.setString("ownerAdminGroup", "");
				props.setString("unprotectAdminGroup", "");
				props.setString("ignoreAdminGroup", "");
				props.setString("detectGroup", "");
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
				sqlTablePrefix = props.getString("sqlTablePrefix");
				dataFolder = props.getString("dataFolder");
				useSQL = props.getBoolean("useSQL");
				protectToolName = props.getString("protectToolName");
				protectToolId = props.getInt("protectToolId");
				detectToolName = props.getString("detectToolName");
				detectToolId = props.getInt("detectToolId");
				allowAdminGroup = props.getString("allowAdminGroup");
				listAdminGroup = props.getString("listAdminGroup");
				ownerAdminGroup = props.getString("ownerAdminGroup");
				unprotectAdminGroup = props.getString("unprotectAdminGroup");
				ignoreAdminGroup = props.getString("ignoreAdminGroup");
				detectGroup = props.getString("detectGroup");
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
		/*	"/protect", " [areaName] [e|c|d|b] - Allows an area to be marked and protected.");
			"/setpermission", " (creatorName) [areaName] -[p|g] [playerName|groupName] [n|e|c|d|b|a] - Allows a player or group to modify an area.");
			"/unprotect" , " (creatorName) [areaName] - optional creatorName if allowed to administrate other player's area. Unprotects area.
			"/listareas" , "- [e|c|d|b|a] (playerName) - Lists areas that meet given permission level. (optional) playerName for administrators.
		*/
			if (!player.canUseCommand(split[0])) {
				return false;
			}
			if (split[0].equalsIgnoreCase("/protect")) {
				String playerName = player.getName();
				int id = -1;
				if (split.length == 2) {
					String areaName = split[1];
					id = BPArea.contains(areaName, playerName);
					if (id == -1) {
						protecting.remove(playerName);
						BPArea area = BPArea.get(id);
						protecting.put(playerName, area);
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
					id = BPArea.contains(areaName, playerName);
				} else {
					id = -2;
					player.sendMessage(Colors.Rose + "Usage: /unprotect [areaName] <ownerName> - Unprotects an area. <ownerName> is for administrative use.");
				}
				if (id == -1) {
					player.sendMessage(Color.Rose + "Invalid area.");
				} else if (id > -1) {
					BPArea temp;
					if ((temp = BPArea.get(id)) && temp.unprotect() && BPArea.delete(temp)) {
						player.sendMessage(Colors.Rose + "Area unprotected.");
					} else {
						player.sendMessage(Colors.Rose + "Error unprotecting area.");
					}
				}
				return true;
			}
			if (split[0].equalsIgnoreCase("/setpermisson")) {
				//"/setpermission", " (creatorName) [areaName] -[p|g] [playerName|groupName] [n|e|c|d|b|a] - Allows a player or group to modify an area.");
				int id = -1;
				String groupName = "";
				String playerName = "";
				if (split.length >= 4) { 
					String ownerName = player.getName();
					if (split.length == 5) {
						if (etc.getServer().matchPlayer(split[1]) != null) {
							ownerName = split[1];
						} else {
							player.sendMessage(Colors.Rose + "Invalid player name.");
							return true;
						}
					}
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
					id = BPArea.exists(areaName, ownerName);
				} else {
					player.sendMessage(Colors.Rose + "Usage: /allow [areaName] -[p|g] [playerName|groupName] <ownerName> Allows a user or group to modify an area.");
				}
				if (id == -1) {
					player.sendMessage(Colors.Rose + "Invalid Area.");
				} else if (id > -1 && group.length > 0) {
					if (allowGroup(id, groupName)) {
						player.sendMessage(Colors.Rose + "Group allowed.");
					} else {
						player.sendMessage(Colors.Rose + "Error allowing group.");
					}
				} else if (id > -1 && playerName.length > 0) {
					if (allowPlayer(id, playerName)) {
						player.sendMessage(Colors.Rose + "Player allowed.");
					} else {
						player.sendMessage(Colors.Rose + "Error allowing player.");
					}
				}
				return true;
			}
			if (split[0].equalsIgnoreCase("/listareas")) {
				String ownerName = player.getName();
				BPArea.permissionLevel level;
				if (split.length >=2) {
					if (split[1].equalsIgnoreCase("a")) {
						level = BPArea.permissionLevel.ADMINISTRATE;
					} else if (split[1].equalsIgnoreCase("b")) {
						level = BPArea.permissionLevel.CREATE_DESTROY;
					} else if (split[1].equalsIgnoreCase("c")) {
						level = BPArea.permissionLevel.CREATE;
					} else if (split[1].equalsIgnoreCase("d")) {
						level = BPArea.permissionLevel.DESTROY;
					} else if (split[1].equalsIgnoreCase("e")) {
						level = BPArea.permissionLevel.ENTER;
					} else {
						player.sendMessage(Colors.Rose+"Invalid permission level chosen.");
						return true;
					}
					if (split.length == 3 && (player.isAdmin() || (listAdminGroup.length > 0 && player.isInGroup(listAdminGroup))) {
						if (etc.getServer().matchName(split[2]) != null) {
							ownerName = split[2];
						} else {
							player.sendMessage(Colors.Rose+"Invalid player name.");
						}
					}
					ArrayList<BPArea> areas = io.getAll(ownerName, level);
					for(BPArea a : areas) {
						player.sendMessage(Colors.Rose+a.getName() + " " + a.getOwner());
					}
					if (areas.size() == 0)
						player.sendMessage(Colors.Rose+"You have not protected any areas yet.");
				} else {
					player.sendMessage(Colors.Rose+"Usage: /listareas - [e|c|d|b|a] (playerName) - Lists areas that meet given permission level. (optional) playerName for administrators.");
				}
				return true;
			}
			return false;
		}

		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand) {	
			if (itemInHand==protectToolId && player.canUseCommand("/protect") && selecting.contains(player.getName())) {
				BPArea t = selecting.get(player.getName());
				if (!t.isStarted()) {
					t.setStart(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
					player.sendMessage(Colors.Rose+"First coordinate selected.");
				} else {
					t.setEnd(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
					if (canProtectArea(t)) {
						if (io.protect(t)) {
							player.sendMessage(Colors.Rose+"Area protected.");
						} else {
							player.sendMessage(Colors.Rose+"Error protecting area.");
						}
					} else {
						player.sendMessage(Colors.Rose+"Cannot protect this area, there is a conflicting protection already in place.");
					}
					selecting.remove(player.getName());
				}
				return true;
			} else if (itemInHand==detectToolId && (detectGroup.equals("") || player.isInGroup(detectGroup))) {
				ArrayList<BPArea> areas = io.getAll(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
				for(BPArea a : areas) {
					player.sendMessage(Colors.Rose+a.getName() + " - " + a.getOwner());
				}
				if (areas.size() == 0)
					player.sendMessage(Colors.Rose+"There are not any protected areas here.");
				return true;
			} else if ( (itemInHand>267 && itemInHand<280) || itemInHand==256 || itemInHand==257 || itemInHand==258 || itemInHand==290 || itemInHand==291|| itemInHand==292|| itemInHand==293 || itemInHand==294 ) {
				//don't handle tools (unless already handled)
				return false;
			}
			return !(player.isInGroup(ignoreAdminGroup) || canModify(player.getName(), blockPlaced, BPArea.permissionLevel.CREATE));
		}
		
		public boolean onBlockDestroy(Player player, Block block) {
			return !(player.isInGroup(ignoreAdminGroup) || canModify(player.getName(), block, BPArea.permissionLevel.DESTROY));
		}
	}
}