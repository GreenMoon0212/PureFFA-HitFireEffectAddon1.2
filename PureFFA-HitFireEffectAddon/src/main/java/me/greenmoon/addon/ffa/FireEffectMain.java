package me.greenmoon.addon.ffa;

import me.bedtwL.ffa.api.EffectAddon;
import org.bukkit.Bukkit;
import java.util.UUID;

public class FireEffectMain implements EffectAddon {

    @Override
    public Integer getAPIVer() { return 1; }

    @Override
    public String getName() { return "KillFireEffect"; }

    @Override
    public String getAuthor() { return "GreenMoon"; }

    @Override
    public UUID authorUUID() {
        return UUID.fromString("53aaa7fb-569e-4391-9323-5762af38f255");
    }

    @Override
    public void onEnable() {
        new KillFireEffect().registerKillEffect(this);
        Bukkit.getPluginManager().registerEvents(new FireEffectProtector(), Bukkit.getPluginManager().getPlugin("PureFFA"));
    }

    @Override
    public void onDisable() {
        new KillFireEffect().unregisterKillEffect();
    }
}
