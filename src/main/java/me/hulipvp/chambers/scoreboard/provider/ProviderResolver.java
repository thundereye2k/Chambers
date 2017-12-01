package me.hulipvp.chambers.scoreboard.provider;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.game.structure.GameStatus;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.scoreboard.ScoreboardProvider;
import me.hulipvp.chambers.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ProviderResolver implements ScoreboardProvider {

	private Chambers plugin;
	private Map<GameStatus, ScoreboardProvider> providers;

	public ProviderResolver() {

		plugin = Chambers.getInstance();
		providers = new HashMap<>();

		LobbyProvider lobbyProvider = new LobbyProvider();
		providers.put(GameStatus.LOBBY, lobbyProvider);
		providers.put(GameStatus.STARTING, lobbyProvider);
		providers.put(GameStatus.INGAME, new IngameProvider());
		providers.put(GameStatus.OVER, new EndProvider());

	}

	@Override
	public String getTitle(Player player) {
		return ChatColor.GOLD.toString() + ChatColor.BOLD + "Chambers" + ChatColor.RESET.toString() + ChatColor.RED + " [BETA]";
	}

	@Override
	public List<String> getLines(Player player) {
		List<String> lines = new ArrayList<>();

		Profile profile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());
		if (profile != null && profile.isHidingScoreboard()) {
			return Collections.emptyList();
		}

		if (this.providers.containsKey(plugin.getGameManager().getGame().getStatus())) {
			lines.addAll(this.providers.get(plugin.getGameManager().getGame().getStatus()).getLines(player));
		}

		if (!lines.isEmpty()) {
			lines.add(0, "&7&m------------------");
			lines.add("&7&m------------------");
		}

		return lines.stream().map(Color::color).collect(Collectors.toList());
	}

}