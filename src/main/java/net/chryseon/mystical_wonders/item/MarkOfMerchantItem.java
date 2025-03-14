package net.chryseon.mystical_wonders.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class MarkOfMerchantItem extends Item {
    public MarkOfMerchantItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!isViable(stack) && entity instanceof PlayerEntity player) {
            gift(stack, player);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (isViable(stack)) {
            tooltip.add(Text.literal("§6Given by " + getGifterName(stack)));
        }
    }

    public static void gift(ItemStack stack, PlayerEntity entity) {
        NbtCompound nbt = stack.getOrCreateSubNbt("owned by chryseon");
        nbt.putUuid("GifterUUID", entity.getUuid());
        nbt.putString("GifterName", entity.getName().getString());
    }

    public static ItemStack copyTo(ItemStack from, ItemStack to) {
        if (isViable(from)) {
            NbtCompound fromNbt = from.getOrCreateSubNbt("owned by chryseon");
            NbtCompound toNbt = to.getOrCreateSubNbt("owned by chryseon");
            toNbt.putUuid("GifterUUID", fromNbt.getUuid("GifterUUID"));
            toNbt.putString("GifterName", fromNbt.getString("GifterName"));
        }
        return to;
    }

    public static boolean isViable(ItemStack stack) {
        if (!stack.hasNbt()) {
            return false;
        } else {
            NbtCompound nbt = stack.getSubNbt("owned by chryseon");
            return nbt != null && nbt.contains("GifterUUID") && nbt.getUuid("GifterUUID") != null;
        }
    }

    public static UUID getGifterUUID(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt("owned by chryseon");
        return isViable(stack) && nbt != null ? nbt.getUuid("GifterUUID") : null;
    }

    public static String getGifterName(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt("owned by chryseon");
        return isViable(stack) && nbt != null ? nbt.getString("GifterName") : "";
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 0;
    }
}