package com.haiytb.blockentitylimiter;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin implements Listener {

    private Map<Material, Integer> blockLimits = new HashMap<>();
    private Map<EntityType, Integer> entityLimits = new HashMap<>();
    private String limitMessage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        for (String key : config.getKeys(false)) {
            if (key.equalsIgnoreCase("Message")) {
                limitMessage = config.getString(key);
                continue;
            }

            try {
                Material material = Material.getMaterial(key.toUpperCase());
                if (material != null) {
                    blockLimits.put(material, config.getInt(key));
                } else {
                    EntityType entityType = EntityType.valueOf(key.toUpperCase());
                    if (entityType != null) {
                        entityLimits.put(entityType, config.getInt(key));
                    } else {
                        getLogger().warning("Invalid block or entity type in config: " + key);
                    }
                }
            } catch (IllegalArgumentException e) {
                getLogger().warning("Invalid block or entity type in config: " + key);
            }
        }

        if (limitMessage == null) {
            limitMessage = "You have reached the limit of this block or entity in this chunk.";
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material blockType = event.getBlock().getType();
        Chunk chunk = event.getBlock().getChunk();

        if (blockLimits.containsKey(blockType)) {
            int limit = blockLimits.get(blockType);
            int count = 0;

            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 256; y++) {
                    for (int z = 0; z < 16; z++) {
                        if (chunk.getBlock(x, y, z).getType() == blockType) {
                            count++;
                            if (count >= limit) {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(limitMessage);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        EntityType entityType = event.getEntityType();
        Chunk chunk = event.getLocation().getChunk();

        if (entityLimits.containsKey(entityType)) {
            int limit = entityLimits.get(entityType);
            int count = 0;

            for (org.bukkit.entity.Entity entity : chunk.getEntities()) {
                if (entity.getType() == entityType) {
                    count++;
                    if (count >= limit) {
                        event.setCancelled(true);
                        if (!event.getLocation().getWorld().getPlayers().isEmpty()) {
                            event.getLocation().getWorld().getPlayers().get(0).sendMessage(limitMessage);
                        }
                        return;
                    }
                }
            }
        }
    }
}