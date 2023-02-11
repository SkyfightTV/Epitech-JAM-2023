package fr.dreamteam.jam.manager;

import org.bukkit.entity.Player;

public class EpiPlayer {

    private final Player player;

    public EpiPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }


}
