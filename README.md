# ShankShock Core

Refining hacked together with each commit.

----

### Redis information:

#### Connection info:

```
redishost: direct.shankshock.com
redispass: request it
redisport: 6379
```

It may be useful to connect via a command line. You should have all information (aside from the password) to do this.

#### Redis storage spec:

1. Minecraft related shit should start with `mc:`.
2. The following is a rough example of where data should be stored. Before using it in production, note it here.
3. Something surrounded in `<brackets>` indicates that you should substitute that with the value you are referring to.
4. Multi-word composed keys should be separated with `.`s. For example: mc:global:my.cats.name
5. A tack `-` indicates the return value of a key, and is not actually used in setting a key. `mc:global:motd - String motd`
6. Redis uses Strings for both keys and values, but it can perform atomic functions on Strings that are actually ints, floats, etc. Essentially, they're more of generic types wrapped in strings.
7. In code, the Redis spec below should exist at all times in all possible ways. The keys should be generated if need be prior to the actual object existing. If a server is being added, generate the values first, then start the server.

```
mc:
	global:
		motd - String motd
		shadow.ban - Boolean true/false
		shadow.kick - Boolean true/false
		chat.format - String format
		user.groups - String (csv) of groups to load permissions for
		xmas - Boolean true/false
		pushover.lock - Locks Pushover notifications for admins
		pushover.clients - Set of Pushover keys to send notifications to
		pushover.key - The application key for Pushover
	server:
		<id> - Int uniqueid (set in config.yml, used as opposed to a serverid for the sake of being able to rename a serverid):
			world:
				<name> - String worldname:
					nobuild - Boolean true/false
					nosilver - Boolean true/false
					silver.multiplier - Decimal 0-1
					block:
						<x.y.z>:
							onuse - clearspleef
							zone - zonename
							typeid - block typeid
	player:
		<name> - String username
			playmins - Minutes played on the server
			namechanged - Set to true when a name has been changed. Expires in 30 days.
			pureloc - x,y,z csv that defines the player's last pure position
			creativeloc - x,y,z csv that defines the player's last pure position
			jailendtime - unix epoch of when the player should be out of jail

```

#### Redis pub/sub spec:
1. Name your channels appropriately.

----

### Commit spec:
* Send tabs, not spaces.
* Send Windows line endings, not unix.
* Follow the [Java conventions published by Oracle](http://www.oracle.com/technetwork/java/codeconv-138413.html) when possible.
* Functions should be like(this) not like ( this ) or like( this )

#### Where to commit to:
* Utility? Tested? Master.
* Utility? Needs testing? Branch it.
* Production code? Tested? Master.
* Production code? Needs testing? Branch it.
* Proof of concept? Branch it.

----

### Filesystem:
1. In config related matters, use the [Bukkit configuration API](http://wiki.bukkit.org/Introduction_to_the_New_Configuration) when possible.
2. Everything else should go in the *ShankShock* folder at the root of the server.
3. Libraries used by the Core should be in the `lib` folder, and denoted as such in the `Manifest.MF` file.

```
CraftBukkit.jar
ShankShock/
	zones.json
	command-blacklist.txt
Plugins/
	ShankShockCore.jar
	Lib/
		netty.jar
		lettuce.jar
```
