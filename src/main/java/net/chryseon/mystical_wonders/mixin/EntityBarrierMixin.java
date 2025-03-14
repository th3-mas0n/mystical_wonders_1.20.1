package net.chryseon.mystical_wonders.mixin;

import net.chryseon.mystical_wonders.MysticalWonders;
import net.chryseon.mystical_wonders.block.entity.BellEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class EntityBarrierMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void preventBarrierEscape(CallbackInfo info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        BlockPos bellPos = getNearestActiveBell(entity.getWorld(), entity.getBlockPos());

        if (bellPos != null && entity.getBlockPos().isWithinDistance(bellPos, 200)) {
            if (!hasMarkOfMerchant(entity)) {
                // Push entity back if they are trying to enter
                pushEntityOut(entity, bellPos);
            }
        }
    }

    private void pushEntityOut(LivingEntity entity, BlockPos bellPos) {
        double dx = entity.getX() - bellPos.getX();
        double dz = entity.getZ() - bellPos.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);

        if (dist < 200) {
            double pushStrength = 0.5; // Adjust for stronger pushback
            entity.setVelocity(dx / dist * pushStrength, 0.2, dz / dist * pushStrength);
        }
    }

    private boolean hasMarkOfMerchant(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            return player.getMainHandStack().isOf(MysticalWonders.MARK_OF_MERCHANT) ||
                    player.getOffHandStack().isOf(MysticalWonders.MARK_OF_MERCHANT) ||
                    player.getInventory().contains(new ItemStack(MysticalWonders.MARK_OF_MERCHANT));
        }
        return false;
    }

    private BlockPos getNearestActiveBell(World world, BlockPos entityPos) {
        BlockPos closest = null;
        double minDistance = Double.MAX_VALUE;

        for (BlockPos bellPos : BellEntity.ACTIVE_BELLS) {
            double distance = bellPos.getSquaredDistance(entityPos);
            if (distance < minDistance) {
                minDistance = distance;
                closest = bellPos;
            }
        }

        return closest;
    }
}