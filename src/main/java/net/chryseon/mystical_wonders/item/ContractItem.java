package net.chryseon.mystical_wonders.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;


public class ContractItem extends Item {

    public ContractItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if(!isViable(stack)) {
            user.setStackInHand(hand, putContract(stack, user));
            stack.getOrCreateNbt().putInt("CustomModelData", 1);
            return TypedActionResult.success(user.getStackInHand(hand));
        } else {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (isViable(stack)) {
            tooltip.add(Text.literal("Signed").formatted(Formatting.OBFUSCATED));
            tooltip.add(Text.literal("Behold a fool named " + getIndebtedName(stack)).formatted(Formatting.GOLD));
            tooltip.add(Text.literal("Retard").formatted(Formatting.OBFUSCATED));

        }
    }

    public static ItemStack putContract(ItemStack stack, PlayerEntity entity) {
        stack.getOrCreateNbt().putUuid("IndebtedUUID", entity.getUuid());
        stack.getOrCreateNbt().putString("IndebtedName", entity.getDisplayName().getString());
        return stack;
    }

    public static ItemStack copyTo(ItemStack from, ItemStack to) {
        if (isViable(from)) {
            to.getOrCreateNbt().putUuid("IndebtedUUID", from.getOrCreateNbt().getUuid("IndebtedUUID"));
            to.getOrCreateNbt().putString("IndebtedName", from.getOrCreateNbt().getString("IndebtedName"));
        }
        return to;
    }

    public static boolean isViable(ItemStack stack) {
        if (!stack.hasNbt()) return false;
        assert stack.getNbt() != null;
        return stack.getNbt().contains("IndebtedUUID") && stack.getOrCreateNbt().getUuid("IndebtedUUID") != null;
    }

    public static void removeDebt(ItemStack stack) {
        if (stack.hasNbt()) {
            stack.getOrCreateNbt().remove("IndebtedUUID");
            stack.getOrCreateNbt().remove("IndebtedName");
        }
    }

    public static UUID getIndebtedUUID(ItemStack stack) {
        if (isViable(stack)) {
            return stack.getOrCreateNbt().getUuid("IndebtedUUID");
        }
        return null;
    }

    public static String getIndebtedName(ItemStack stack) {
        if (isViable(stack)) {
            return stack.getOrCreateNbt().getString("IndebtedName");
        }
        return "";
    }

}
