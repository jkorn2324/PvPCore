# PvPCore - PMMP
This is a plugin for the server software, PocketMine-MP, based on my PvPCore plugin for the server software, Nukkit.

## Table of Contents
<!--ts-->
* [PvPCore - PMMP](#gh-md-toc)
   * [Table of Contents](#table-of-contents)
   * [Features](#features)
   * [Configuration](#configuration)
      * [Commands](#commands)
      * [Permissions](#permissions)
      * [Using The Forms](#using-the-forms)
      * [Configuration-Values](#configuration-values)
      * [Recommended-Configurations](#recommended-configurations)
<!--te-->

## Features
This plugin allows you to easily change the knockback the players take in a specific level or an area within the level. This plugin changed from previous versions to allow the user to change the horizontal and vertical knockback, as well as the attack speed. 

**List of Features:**
- [x] Customizable Knockback
	- [x] Horizontal Knockback
	- [x] Vertical Knockback
	- [x] Attack Delay
- [x] Knockback affecting an World.
- [x] Knockback Affecting an Area
- [x] Multi-Version Support
	- [x] 1.14.60 Support
	- [x] 1.16.0 Support

## Configuration
Fortunately, it's now much easier to configure the Knockback as compared to previous versions of KitKB. Instead of using commands, users now utilize the pvpcore **form menu.**

### Commands 
Not everything required to configure the plugin utilizes the menu. The commands that are used have changed significantly since the previous version as a result of the menu. Even so, there are still commands that you should know about. 

- `/pvpcore` - This command sends the main PvPCore Form Menu to the user. This only works with players that have the appropriate permissions on the server.
- `/pvparea <pos1:pos2>` - The command that allows the user to set the first and second position boundaries for the PvPAreas that they are going to create. This command sets the area based on the **player's position.**
	- `/pvparea pos1` - Sets the first position of the PvPArea.
	- `/pvparea pos2` - Sets the second position of the PvPArea.
- `/editWorld` - This command sends the form menu that allows the user to edit the knockback in the world. **This is a shortcut command, as this menu is also available in the PvPCore menu.**
- `/createArea` - This command sends the form menu that allows the user to create a new PvParea. **This is a shortcut command, as this menu is also available in the PvPCore menu.**

### Permissions
The amount of permissions within the plugin has been siginficantly reduced since the previous version. Nonetheless, here are the permissions.
- `pvpcore.permission.edit` - This is the permission used by the plugin to determine whether or not the user can edit the Knockback at all. **By default, its only available to people with operator permissions.**

### Using the Forms
As stated earlier, using the command `/pvpcore` displays the main PvPCore form menu. When the PvPCore menu is open, you can:
- Edit the knockback for a specific World
- View the knockback for a specific World
- Edit the knockback for a specific PvPArea that you created.
- View the configuration for a specific PvPArea.
- Delete a PvPArea.

### Configuration Values
For each world and pvp-area, there are specific types of variables that you can configure. This section is to help you understand what each configuration does.
- `Knockback-Enabled` - This determines whether the custom knockback that you assigned for a particular world or PvPArea is enabled or not. **By Default, this is enabled.**
- `Horizontal-Knockback` - This determines how far away the player goes during each hit. The higher the value, the farther away the player goes. **By Default, the horizontal knockback is set to 0.4.**
- `Vertical-Knockback` - This determines how high the player goes during each hit. The higher the value, the higher the player goes. **By Default, the Vertical Knockback is set to 0.4.**
- `Attack-Delay` - This determines the amount of time **between hits** where you can't hit the player. The higher the value, the higher the amount of time between each hit. **The attack delay is measured in Minecraft ticks (20 Ticks = 1 Second). By Default, the attack delay is set to 10 (1/2 Second).** 

### Recommended Configurations
For reference, here are some recommended KB Configurations that users of this plugin have given me:

| **Type**  | **Horizontal-KB** | **Vertical-KB**  | **Attack-Delay** |
| ------------ | ------------ | ------------ | ------------ |
| Default  | 0.4 | 0.4 | 10  |
| Fist  | 0.42  | 0.42  | 9  |
| Combo  | 0.29  | 0.29  | 2  |


