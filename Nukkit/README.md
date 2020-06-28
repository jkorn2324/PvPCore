# PvPCore
## Supports custom knockback for Nukkit.
### Config:
#### Worlds: 
##### knockback-y -> Toggles the height of player knockback.
##### knockback-xz -> Toggles the length of player knockback. 
##### custom-kb -> Determines whether the default nukkit KB is used or whether the custom KB is used.
##### attack-delay -> Determines the attack delay in between hits (Default = 10, Combo = 3 or 4)
#### Areas:
##### attack-delay -> Determines the attack delay in between hits for that specific area. (Default = 10, Combo = 3 or 4)
##### kb-enabled -> Determines whether the knockback is enabled for that specific area.
##### world -> Specifies the level it belongs in.
##### first-pos -> The first  position in the world that represents the first boundary of the square area.
##### second-pos -> The second position in the world that represents the other boundary of the the square area. 
##### knockback-y -> Determines the height of the player knockback.
##### knockback-xz -> Determines the length of the player knockback.
### Commands:
#### Area:
##### /area help -> Lists all of the area commands.
##### /area create [name] -> Creates a new area at the current position.
##### /area delete [name] -> Deletes the area.
##### /area setpos [min | max] [name] -> Sets the bounds of the area (min = minimum bound or position 1, max = maximum bound or position 2)
##### /area list -> Lists all of the areas on the server.
#### PvP:
##### /pvp help -> Lists all of the pvp commands.
##### /pvp enable [level | area] [name] -> enables your custom pvp configurations in the level or area specified in the parameter.
##### /pvp disable [level | area] [name] -> disables you custom pvp configurations in the level or area specified in the parameter.
##### /pvp set [level | area] [name] [knockback | attackdelay] [delay | xz-kb] [y-kb] -> Configures the knockback settings of a level or an area.
###### * Examples of /pvp set :  
###### /pvp set level world knockback 0.3 0.5 
###### -> Sets the knockback of the level 'world' to have a knockback length of 0.3, and a knockback height of 0.5.
###### /pvp set area myarea attackdelay 8
###### -> Sets the knockback of the area called 'myarea' to have an attackdelay of 8.