package net.chryseon.mystical_wonders.item;

import net.chryseon.mystical_wonders.MysticalWonders;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class HandbellItem extends Item {
    private static final int BARRIER_DURATION = 6000; // 5 minutes (in ticks)

    public HandbellItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null || player.getWorld().isClient) return ActionResult.SUCCESS;

        UUID playerId = player.getUuid();

        // Check if the player already has a barrier active
        boolean isActive = MysticalWonders.ACTIVE_PLAYERS.getOrDefault(playerId, false);

        if (isActive) {
            player.sendMessage(Text.of("The barrier is already active!"), true);
            return ActionResult.SUCCESS;
        }

        // Activate the barrier
        MysticalWonders.activateBarrier(player, BARRIER_DURATION);
        player.sendMessage(Text.of("Barrier activated! 🛡️"), true);

        // Play Handbell sound
        World world = player.getWorld();
        world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);

        return ActionResult.SUCCESS;
    }

    public static void spawnBarrier(World world, BlockPos center) {
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos pos = center.add(x, y, z);

                    // Spawn Soul Flame Particles around the player
                    if (world.isClient) {
                        world.addParticle(
                                net.minecraft.particle.ParticleTypes.SOUL_FIRE_FLAME,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                (Math.random() - 0.5) * 0.1,
                                (Math.random() - 0.5) * 0.1,
                                (Math.random() - 0.5) * 0.1
                        );
                    }
                }
            }
        }
    }

    public static void removeBarrier(World world, BlockPos center) {
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos pos = center.add(x, y, z);
                    if (world.getBlockState(pos).isOf(Blocks.BARRIER)) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }
}
