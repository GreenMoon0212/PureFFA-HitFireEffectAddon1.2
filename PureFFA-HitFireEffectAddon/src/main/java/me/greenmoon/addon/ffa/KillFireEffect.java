package me.greenmoon.addon.ffa;

import me.bedtwL.ffa.api.effect.PureKillEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class KillFireEffect extends PureKillEffect {

    @Override
    public void killEffect(Location location, Player player) {
        FireEffectProtector.activateGlobalProtection(3000L, Bukkit.getPluginManager().getPlugin("PureFFA"));
        spawnFireLayers(location);
    }

    @Override
    public ItemStack getItemBase() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

    @Override
    public String getName() {
        return "Fire";
    }

    @Override
    public String getItemNameKey() {
        return "Fire-explode";
    }

    private void spawnFireLayers(Location center) {
        final int layers = 6;
        final long delayPerLayer = 2L;

        for (int i = 0; i < layers; i++) {
            final int layer = i;

            Bukkit.getScheduler().runTaskLater(
                    Bukkit.getPluginManager().getPlugin("PureFFA"),
                    () -> {
                        double radius = 1 + layer;
                        List<Block> fireBlocks = new ArrayList<>();
                        Map<Block, Material> replacedBlocks = new HashMap<>();

                        for (int j = 0; j < 32; j++) {
                            double angle = 2 * Math.PI * j / 32;
                            double x = center.getX() + radius * Math.cos(angle);
                            double z = center.getZ() + radius * Math.sin(angle);
                            Location fireLoc = new Location(center.getWorld(), x, center.getY(), z).getBlock().getLocation();

                            Block fireBlock = fireLoc.getBlock();
                            Block belowBlock = fireBlock.getRelative(0, -1, 0);

                            if (fireBlock.getType() == Material.AIR) {
                                if (belowBlock.getType() == Material.AIR) {
                                    continue;
                                }

                                if (!canSupportFire(belowBlock.getType())) {
                                    replacedBlocks.put(belowBlock, belowBlock.getType());
                                    belowBlock.setType(Material.NETHERRACK);
                                }

                                fireBlock.setType(Material.FIRE);
                                fireBlocks.add(fireBlock);
                            }
                        }

                        Bukkit.getScheduler().runTaskLater(
                                Bukkit.getPluginManager().getPlugin("PureFFA"),
                                () -> {
                                    for (Block block : fireBlocks) {
                                        if (block.getType() == Material.FIRE) {
                                            block.setType(Material.AIR);
                                        }
                                    }
                                    for (Map.Entry<Block, Material> entry : replacedBlocks.entrySet()) {
                                        Block block = entry.getKey();
                                        block.setType(entry.getValue());
                                    }
                                },
                                2L
                        );
                    },
                    layer * delayPerLayer
            );
        }
    }

    private boolean canSupportFire(Material material) {
        switch (material) {
            case AIR:
            case GLASS:
            case ICE:
            case BARRIER:
            case STAINED_GLASS:
            case GLOWSTONE:
                return false;
            default:
                return material.isSolid();
        }
    }
}
