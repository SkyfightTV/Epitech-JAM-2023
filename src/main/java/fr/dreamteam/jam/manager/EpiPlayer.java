package fr.dreamteam.jam.manager;

import org.bukkit.entity.Player;

public class EpiPlayer {

    private final Player player;
    private Role role;

    public EpiPlayer(Player player) {
        this.player = player;
    }

    public EpiPlayer(Player player, Role role) {
        this.player = player;
        this.role = role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return this.role;
    }

    public Player getPlayer() {
        return this.player;
    }


}
