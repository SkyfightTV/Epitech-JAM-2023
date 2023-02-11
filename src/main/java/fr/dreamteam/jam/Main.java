package fr.dreamteam.jam;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("Hello World!");
    }

    @Override
    public void onDisable() {
        System.out.println("Goodbye World!");
    }

    public static Main getInstance() {
        return instance;
    }
}
