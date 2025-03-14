package net.chryseon.mystical_wonders.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeleporterItem extends Item {
    private static final String NBT_DIMENSION = "TeleportDimension";
    private static final String NBT_X = "TeleportX";
    private static final String NBT_Y = "TeleportY";
    private static final String NBT_Z = "TeleportZ";

    public TeleporterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (player.isSneaking()) {
            // Clear teleport location
            stack.removeSubNbt(NBT_DIMENSION);
            stack.removeSubNbt(NBT_X);
            stack.removeSubNbt(NBT_Y);
            stack.removeSubNbt(NBT_Z);
            player.sendMessage(Text.literal("Teleport location cleared").formatted(Formatting.YELLOW), true);
        } else {
            if (stack.getOrCreateNbt().contains(NBT_X)) {
                // Teleport player to saved location
                teleportPlayer(player, stack);
                // Clear the stored location
                stack.removeSubNbt(NBT_DIMENSION);
                stack.removeSubNbt(NBT_X);
                stack.removeSubNbt(NBT_Y);
                stack.removeSubNbt(NBT_Z);
            } else {
                // Store current location
                saveLocation(player, stack);
            }
        }

        return TypedActionResult.success(stack);
    }

    private void saveLocation(PlayerEntity player, ItemStack stack) {
        BlockPos pos = player.getBlockPos();
        RegistryKey<World> dimension = player.getWorld().getRegistryKey();

        stack.getOrCreateNbt().putString(NBT_DIMENSION, dimension.getValue().toString());
        stack.getOrCreateNbt().putInt(NBT_X, pos.getX());
        stack.getOrCreateNbt().putInt(NBT_Y, pos.getY());
        stack.getOrCreateNbt().putInt(NBT_Z, pos.getZ());

        player.sendMessage(Text.literal("Location bound: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ())
                .formatted(Formatting.GOLD), true);
    }

    private void teleportPlayer(PlayerEntity player, ItemStack stack) {
        if (stack.getOrCreateNbt().contains(NBT_X)) {
            int x = stack.getNbt().getInt(NBT_X);
            int y = stack.getNbt().getInt(NBT_Y);
            int z = stack.getNbt().getInt(NBT_Z);

            player.teleport(x + 0.5, y, z + 0.5);
            player.sendMessage(Text.literal("Teleported to: " + x + ", " + y + ", " + z)
                    .formatted(Formatting.YELLOW), true);
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof PlayerEntity && stack.getOrCreateNbt().contains(NBT_X)) {
            teleportPlayer((PlayerEntity) target, stack);
            // Clear stored location
            stack.removeSubNbt(NBT_DIMENSION);
            stack.removeSubNbt(NBT_X);
            stack.removeSubNbt(NBT_Y);
            stack.removeSubNbt(NBT_Z);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.getOrCreateNbt().contains(NBT_X)) {
            int x = stack.getNbt().getInt(NBT_X);
            int y = stack.getNbt().getInt(NBT_Y);
            int z = stack.getNbt().getInt(NBT_Z);
            tooltip.add(Text.literal("Bound Location: " + x + ", " + y + ", " + z).formatted(Formatting.YELLOW));
        } else {
            tooltip.add(Text.literal("Right-click to bind a location").formatted(Formatting.GOLD));
        }
    }
}