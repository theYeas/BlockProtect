/*
	BlockProtectPlugin.java - Basic plugin interface.
*/
/*-
	BlockProtect - A hMod plugin to allow protection of areas.
	Copyright (C) 2010  Matthew Wiese

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.util.logging.Logger;
import java.io.File;

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
	public String protectAdminGroup = "";
	public String unprotectAdminGroup = "";
	public String ignoreAdminGroup = "";
	public String detectGroup = "";
	public String sqlTablePrefix = "bp_";
	public String dataFolder = File.separator+"bp";
	public boolean useSQL = true;
	public BPArea.permissionLevel defaultPermissionLevel = BPArea.permissionLevel.NONE;
	public BPArea.permissionLevel globalPermissionLevel = BPArea.permissionLevel.NONE;
	private _srvpropsChecked = false;
	private _configFolder = "";
	BPio io = null;
	
	priavte HashMap<String, BPArea> selecting = null;
	
	public void enable() {//updateme
		log.info(name + " " + version + " Plugin Enabled.");
		etc.getInstance().addCommand("/protect", " [areaName] [n|e|c|d|b] - Allows an area to be marked and protected.");
		etc.getInstance().addCommand("/setpermission", " (creatorName) [areaName] -[p|g] [playerName|groupName] [n|e|c|d|b|a] - Allows a player or group to modify an area.");
		etc.getInstance().addCommand("/unprotect" , " (creatorName) [areaName] - optional creatorName if allowed to administrate other player's area. Unprotects area.");
		etc.getInstance().addCommand("/listareas" , " [e|c|d|b|a] (playerName) - Lists areas that meet given permission level. (optional) playerName for administrators.");
	}

	public void disable() {
		log.info(name + " " + version + " Plugin Disabled.");
		etc.getInstance().addCommand("/protect");
		etc.getInstance().addCommand("/allow");
		etc.getInstance().addCommand("/unprotect");
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
		//add movement blocking code!
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
				props.setString("ownerAdminGroup", "")
				props.setString("protectAdminGroup", "");;
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
				protectAdminGroup = props.getString("protectAdminGroup");
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
		/*	"/protect", " [areaName] [n|e|c|d|b] - Allows an area to be marked and protected.");
			"/setpermission", " (creatorName) [areaName] -[p|g] [playerName|groupName] [n|e|c|d|b|a] - Allows a player or group to modify an area.");
			"/unprotect" , " (creatorName) [areaName] - optional creatorName if allowed to administrate other player's area. Unprotects area.");
			"/listareas" , " [e|c|d|b|a] (playerName) - Lists areas that meet given permission level. (optional) playerName for administrators.");
			
			//possible commands
			"/changecreator" , " (creatorName) [areaName] [playerName] - Transfers true ownership to another player.
			
			
			/savearea - saves area to the db, only one save per area!
			/exportsave - save save to WE file
			/revertarea - reverts area to previous save
			
			/fill - tells to fill an area
			/shell - makes a shell of around an area
			
		*/
			if (!player.canUseCommand(split[0])) {
				return false;
			}
			if (split[0].equalsIgnoreCase("/protect")) {//should be done
				if (split.length == 3) {					
					String playerName = player.getName();
					BPArea.permissionLevel level;
					if (split[1].equalsIgnoreCase("b")) {
						level = BPArea.permissionLevel.CREATE_DESTROY;
					} else if (split[1].equalsIgnoreCase("c")) {
						level = BPArea.permissionLevel.CREATE;
					} else if (split[1].equalsIgnoreCase("d")) {
						level = BPArea.permissionLevel.DESTROY;
					} else if (split[1].equalsIgnoreCase("e")) {
						level = BPArea.permissionLevel.ENTER;
					} else if (split[1].equalsIgnoreCase("n")) {
						level = BPArea.permissionLevel.NONE;
					} else {
						player.sendMessage(Colors.Rose+"Invalid permission level chosen.");
						return true;
					}
					String areaName = split[1];
					BPArea area = io.get(areaName, playerName);
					if (area == null) {
						protecting.remove(playerName);
						BPArea area = new BPArea(areaName, playerName, level);
						protecting.put(playerName, area);
						player.sendMessage(Colors.Rose + "Choose coordinates by right clicking blocks with a "+toolName+".");
					} else {
						player.sendMessage(Colors.Rose + "Area name already exisits.");
					}
				} else {
					player.sendMessage(Colors.Rose + "Usage: [areaName] [n|e|c|d|b] - Allows an area to be marked and protected.");
				}
				return true;
			}
			if (split[0].equalsIgnoreCase("/unprotect")) {//should be done
				if (split.length >= 2) {
					String areaName = split[1];
					String ownerName = player.getName();
					if (split.length == 3) {
						if (etc.getServer().matchPlayer(split[2]) != null) {
							ownerName = split[2];
						} else {
							player.sendMessage(Color.Rose + "Invalid player name.");
							return true;
						}
					}
					BPArea area = io.get(areaName, ownerName);
					if (area != null) {
						if (player.isAdmin() || (unprotectAdminGroup.length() > 0 && player.isInGroup(unprotectAdminGroup)) || io.playerAllowed(area, player.getName(), BPArea.permissionLevel.ADMINISTRATE)) {
							if (io.uprotect(area)) {
								player.sendMessage(Color.Rose + "Area unprotected.");
							} else {
								player.sendMessage(Color.Rose + "Error unprotecting area.");
							}
						} else {
							player.sendMessage(Color.Rose + "You do not have permission to unprotect that area.");
						}
					} else {
						player.sendMessage(Color.Rose + "Invalid area.");
					}
				} else {
					player.sendMessage(Colors.Rose + "Usage: (creatorName) [areaName] - optional creatorName if allowed to administrate other player's area. Unprotects area.");
				}
				return true;
			}
			if (split[0].equalsIgnoreCase("/setpermisson")) { //should be done
				//"/setpermission", " (creatorName) [areaName] -[p|g] [playerName|groupName] (n|e|c|d|b|a) - Allows a player or group to modify an area.");
				if (split.length >= 4) { 
					String ownerName = player.getName();
					int i = 0;
					if (split.length == 6 || (split.length == 5 && split[split.length].length != 1)) {
						i = 1;
						if (etc.getServer().matchPlayer(split[1])) {
							ownerName = split[1];
						} else {
							player.sendMessage(Colors.Rose + "Invalid player name.");
							return true;
						}
					}
					BPArea.permissionLevel level = defaultPermissionLevel;
					if (split.length == 6 || (split.length == 5 && split[split.length].length == 1)) {
						if (split[i+5].equalsIgnoreCase("a")) {
							level = BPArea.permissionLevel.ADMINISTRATE;
						} else if (split[i+5].equalsIgnoreCase("b")) {
							level = BPArea.permissionLevel.CREATE_DESTROY;
						} else if (split[i+5].equalsIgnoreCase("c")) {
							level = BPArea.permissionLevel.CREATE;
						} else if (split[i+5].equalsIgnoreCase("d")) {
							level = BPArea.permissionLevel.DESTROY;
						} else if (split[i+5].equalsIgnoreCase("e")) {
							level = BPArea.permissionLevel.ENTER;
						} else if (split[i+5].equalsIgnoreCase("n")) {
							level = BPArea.permissionLevel.NONE;
						} else {
							player.sendMessage(Colors.Rose+"Invalid permission level chosen.");
							return true;
						}
					}
					String areaName = split[i+1];
					String playerName = split[i+3];
					BPArea area = io.get(areaName, ownerName);
					if (area != null) {
						if (player.isAdmin() || (listAdminGroup.length() > 0 && player.inGroup(listAdminGroup)) || io.playerAllowed(area, player.getName(), BPArea.permissionLevel.ADMINISTRATE)) {
							if (split[i+2].equalsIgnoreCase("-p")) {
								if (etc.getServer().matchPlayer(playerName) != null) {
									io.allowPlayer(area, playerName, level);
								} else {
									player.sendMessage(Colors.Rose + "Invalid player name.");
								}
							} else if (split[i+2].equalsIgnoreCase("-g")) {
								if (io.validGroupName(playerName)) { //how to verify group quickly
									io.allowGroup(area, playerName, level);
								} else {
									player.sendMessage(Colors.Rose + "Invalid group name.");
								}
							} else {
								player.sendMessage(Colors.Rose + "Invalid p|g flag.");
							}
						} else {
							player.sendMessage(Colors.Rose + "You do not have permission to edit permissions for that area.");
						}
					} else {
						player.sendMessage(Colors.Rose + "Cannot find area.");
					}
				} else {
					player.sendMessage(Colors.Rose + "Usage: /setpermission (creatorName) [areaName] -[p|g] [playerName|groupName] [n|e|c|d|b|a] - Allows a player or group to modify an area.");
				}
				return true;
			}
			if (split[0].equalsIgnoreCase("/listareas")) { //should be done
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
					} else if (split[1].equalsIgnoreCase("n")) {
						level = BPArea.permissionLevel.NONE;
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

		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand) { //should be done	
			if (itemInHand==protectToolId && player.canUseCommand("/protect") && selecting.contains(player.getName())) {
				BPArea t = selecting.get(player.getName());
				if (!t.isStarted()) {
					t.setStart(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
					player.sendMessage(Colors.Rose+"First coordinate selected.");
				} else {
					t.setEnd(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
					if (player.isAdmin() || (protectAdminGroup.length() > 0 && player.isInGroup(protectAdminGroup)) || !io.isAdminInAllAreas(area)) {
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
		
		public boolean onBlockDestroy(Player player, Block block) { //should be done
			return !(player.isInGroup(ignoreAdminGroup) || canModify(player.getName(), block, BPArea.permissionLevel.DESTROY));
		}
	}
}