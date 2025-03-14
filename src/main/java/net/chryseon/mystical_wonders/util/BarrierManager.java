package net.chryseon.mystical_wonders.util;

import net.chryseon.mystical_wonders.item.HandbellItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class BarrierManager {
    private static final HashMap<UUID, Integer> activeBarriers = new HashMap<>();

    public static void activateBarrier(PlayerEntity player, int duration) {
        UUID playerId = player.getUuid();
        activeBarriers.put(playerId, duration);
    }

    public static boolean isBarrierActive(PlayerEntity player) {
        return activeBarriers.containsKey(player.getUuid());
    }

    public static void handleBarriers(World world) {
        activeBarriers.forEach((playerId, ticksLeft) -> {
            if (ticksLeft <= 0) {
                activeBarriers.remove(playerId);
            } else {
                PlayerEntity player = world.getPlayerByUuid(playerId);
                if (player != null) {
                    BlockPos playerPos = player.getBlockPos();
                    HandbellItem.spawnBarrier(world, playerPos);
                    activeBarriers.put(playerId, ticksLeft - 1);
                }
            }
        });
    }
}
