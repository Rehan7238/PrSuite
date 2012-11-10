package me.mikey.com;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PrMain extends JavaPlugin implements Listener {

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		System.out.println("[PrSuite] PrSuite has been enabled!");
	}

	public void onDisable() {
		System.out.println("[PrSuite] PrSuite has been disabled!");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		String playername = player.getName();

		// Home goes to last slept in bed
		if (cmd.getName().equalsIgnoreCase("home")) {
			if (player.getBedSpawnLocation() != null) {
				Location Home = player.getBedSpawnLocation();
				Location loc = player.getLocation();
				World world = loc.getWorld();
				player.sendMessage(ChatColor.GRAY
						+ "You click your heels together, and whisper");
				player.sendMessage(ChatColor.GREEN
						+ "\"Theres no place like home\"");
				world.playEffect(loc, Effect.EXTINGUISH, 20);
				world.playEffect(loc, Effect.SMOKE, 80);
				player.teleport(Home);
			} else {
				player.sendMessage(ChatColor.RED
						+ "You must sleep in a bed, to have a home, silly!");
			}
		}

		// a before text to speak in Admin Chat!
		if (cmd.getName().equalsIgnoreCase("a")) {

			if (!sender.hasPermission("command.adminchat") && !sender.isOp()) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission to use this!");
				return true;
			}

			if (args.length < 1) {
				return false;
			}

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				sb.append(args[i]).append(" ");
			}

			String msg = sb.toString().trim();
			String prefix = (ChatColor.GRAY + "[" + ChatColor.GOLD + "Staff"
					+ ChatColor.GRAY + "] ");
			String outputformat = (prefix + ChatColor.YELLOW + playername
					+ ChatColor.WHITE + ": " + msg);

			for (Player user : Bukkit.getServer().getOnlinePlayers()) {
				if (user.hasPermission("command.adminchat")) {
					user.sendMessage(outputformat);
				}
			}
		}

		// end onCommand Here!
		return true;
	}

	// Start Listeners Here!
	@EventHandler
	public void onSleep(PlayerBedEnterEvent event) {
		Player player = event.getPlayer();
		player.setHealth(20);
		player.setCompassTarget(event.getBed().getLocation());
		if (player.getBedSpawnLocation() == null) {
			player.sendMessage(ChatColor.GREEN
					+ "Home Bed location set, use /home to return here!");
		} else {
			player.sendMessage(ChatColor.GRAY
					+ "You lay down and rest your head.");
		}
	}

	@EventHandler
	// Changes the message of on join, and adds some effects
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName();
		Location loc = player.getLocation();
		World world = loc.getWorld();
		world.playEffect(loc, Effect.GHAST_SHOOT, 20);
		world.playEffect(loc, Effect.ENDER_SIGNAL, 80);
		event.setJoinMessage(ChatColor.YELLOW + "* " + ChatColor.DARK_GREEN
				+ playername + ChatColor.GRAY + " has joined the game.");
	}

	@EventHandler
	// Changes the message of on quit, and adds some effects
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName();
		Location loc = player.getLocation();
		World world = loc.getWorld();
		world.playEffect(loc, Effect.GHAST_SHOOT, 20);
		world.playEffect(loc, Effect.MOBSPAWNER_FLAMES, 80);
		event.setQuitMessage(ChatColor.YELLOW + "* " + ChatColor.RED
				+ playername + ChatColor.GRAY + " has left the game.");
	}

	@EventHandler
	// Disables the teleportation of Dragon Eggs on left, and right click
	public void PlayerInteractUseDragon(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if (((event.getAction() == Action.RIGHT_CLICK_BLOCK)
				|| (event.getAction() == Action.LEFT_CLICK_AIR) || (event
				.getAction() == Action.LEFT_CLICK_BLOCK))
				&& (block != null)
				&& (block.getTypeId() == 122)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	// Killer of dragon gets announced, and recieves a dragon egg.
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntityType() == EntityType.ENDER_DRAGON) {
			Player killer = event.getEntity().getKiller();
			String killername = killer.getName();
			killer.getInventory().addItem(new ItemStack(122));
			Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + killername
					+ " slayed the Ender Dragon!");
		}
	}

	@EventHandler
	// Disables the creation of Dragon Fountains during dragon raids
	public void onFountainCreating(EntityCreatePortalEvent event) {
		event.setCancelled(true);
		event.getBlocks().clear();
	}

	@EventHandler
	// No enchanting tables
	public void onAccessEnchant(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if (((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (block
				.getTypeId() == 116))) {
			event.getPlayer()
					.sendMessage(
							ChatColor.RED
									+ "Enchanting Tables are disallowed for the time being!");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlaceBlock(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

		}
	}

	// end plugin
}
