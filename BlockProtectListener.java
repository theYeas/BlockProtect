/*
	BlockProtectPlugin.java - Listener to handle interaction with players.
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

import java.util.ArrayList;

public class BlockProtectListener extends PluginListener {
	BlockProtectPlugin p;
	BPio io;
	
	public BlockProtectListener(BlockProtectPlugin plugin, BPio io) {
		p = plugin;
		this.io = io;
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
					if (player.isAdmin() || (p.unprotectAdminGroup.length() > 0 && player.isInGroup(p.unprotectAdminGroup)) || io.playerAllowed(area, player.getName(), BPArea.permissionLevel.ADMINISTRATE)) {
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
					if (player.isAdmin() || (p.listAdminGroup.length() > 0 && player.inGroup(p.listAdminGroup)) || io.playerAllowed(area, player.getName(), BPArea.permissionLevel.ADMINISTRATE)) {
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
				if (split.length == 3 && (player.isAdmin() || (p.listAdminGroup.length > 0 && player.isInGroup(p.listAdminGroup))) {
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
		if (itemInHand == p.protectToolId && player.canUseCommand("/protect") && selecting.contains(player.getName())) {
			BPArea t = selecting.get(player.getName());
			if (!t.isStarted()) {
				t.setStart(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
				player.sendMessage(Colors.Rose+"First coordinate selected.");
			} else {
				t.setEnd(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
				if (player.isAdmin() || (p.protectAdminGroup.length() > 0 && player.isInGroup(p.protectAdminGroup)) || io.isAdminInAllAreas(area)) {
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
		return !(p.ignoreAdminGroup.length() > 0 && player.isInGroup(p.ignoreAdminGroup) || io.playerAllowed(p.blockPlaced.getX(), blockPlaced.getY(), blockPlaced.getZ(), player.getName(), BPArea.permissionLevel.CREATE));
	}
	
	public boolean onBlockDestroy(Player player, Block block) { //should be done
		return !(p.ignoreAdminGroup.length() > 0 && player.isInGroup(p.ignoreAdminGroup) || canModify(blockPlaced.getX(), blockPlaced.getY(), blockPlaced.getZ(), player.getName(), BPArea.permissionLevel.DESTROY));
	}
}