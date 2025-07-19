package me.greenmoon.addon.ffa;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class FireEffectProtector implements Listener {

    private static volatile boolean fireProtectionActive = false;

    public static void activateGlobalProtection(long ticks, Plugin plugin) {
        fireProtectionActive = true;
        Bukkit.getScheduler().runTaskLater(plugin, () -> fireProtectionActive = false, ticks);
    }

    public static boolean isFireProtectionActive() {
        return fireProtectionActive;
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (fireProtectionActive) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if (fireProtectionActive) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCombust(EntityCombustEvent event) {
        if (fireProtectionActive) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCombustByBlock(EntityCombustByBlockEvent event) {
        if (fireProtectionActive) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFireDamage(EntityDamageEvent event) {
        if (fireProtectionActive && (
                event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                        event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)) {
            event.setCancelled(true);
        }
    }
}
