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
import java.util.logging.Level;
import java.io.File;

public class BlockProtectPlugin extends Plugin {
	public static final Logger log = Logger.getLogger("Minecraft");
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
	BlockProtectListener l;
	
	
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
		l = new BlockProtectListener(this, io);
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
}