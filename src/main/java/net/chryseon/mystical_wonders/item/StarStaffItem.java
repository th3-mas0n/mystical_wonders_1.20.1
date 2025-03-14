package net.chryseon.mystical_wonders.item;

import net.chryseon.mystical_wonders.util.PlayerEntityDataSaver;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class StarStaffItem extends SwordItem {
    private static final int WEAK_BEAM_COOLDOWN = 100;
    private static final int ANNIHILATION_COOLDOWN = 600;
    private static final int EXPLOSION_COOLDOWN = 1200;

    public StarStaffItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            NbtCompound data = ((PlayerEntityDataSaver) player).getPersistentData();
            int solarKills = data.getInt("solar_kills");

            if (solarKills >= 20) {
                if (!player.getItemCooldownManager().isCoolingDown(this)) {
                    createSolarExplosion(world, player);
                    data.putInt("solar_kills", 0); // Reset kills
                    player.getItemCooldownManager().set(this, EXPLOSION_COOLDOWN);
                } else {
                    player.sendMessage(Text.literal("The Ashen-Dawn needs more time to recharge...").formatted(Formatting.GOLD), true);
                }
            } else if (solarKills >= 10) {
                if (!player.getItemCooldownManager().isCoolingDown(this)) {
                    callSolarAnnihilationBeam(world, player);
                    data.putInt("solar_kills", 0); // Reset kills
                    player.getItemCooldownManager().set(this, ANNIHILATION_COOLDOWN);
                } else {
                    player.sendMessage(Text.literal("The Ashen-Dawn is still recovering...").formatted(Formatting.YELLOW), true);
                }
            } else {
                if (!player.getItemCooldownManager().isCoolingDown(this)) {
                    fireWeakSunBeam(world, player);
                    player.getItemCooldownManager().set(this, WEAK_BEAM_COOLDOWN);
                } else {
                    player.sendMessage(Text.literal("The staff is recharging...").formatted(Formatting.GOLD), true);
                }
            }
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }

    private void fireWeakSunBeam(World world, PlayerEntity player) {
        Vec3d targetPos = getLookingPos(player);
        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
        world.createExplosion(null, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.5f, World.ExplosionSourceType.MOB);
    }

    private void callSolarAnnihilationBeam(World world, PlayerEntity player) {
        Vec3d targetPos = getLookingPos(player);
        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
        lightning.refreshPositionAfterTeleport(targetPos.getX(), targetPos.getY(), targetPos.getZ());
        world.spawnEntity(lightning);
        player.sendMessage(Text.literal("The Ashen-Dawn judges your enemies...").formatted(Formatting.GOLD), true);
    }

    private void createSolarExplosion(World world, PlayerEntity player) {
        Vec3d targetPos = getLookingPos(player);
        world.createExplosion(null, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 5.0f, World.ExplosionSourceType.MOB);
        player.sendMessage(Text.literal("The Ashen-Dawn obliterates everything in its path! 🌋").formatted(Formatting.GOLD), true);
    }

    private Vec3d getLookingPos(PlayerEntity player) {
        HitResult hitResult = player.raycast(50, 0.0f, false);
        if (hitResult instanceof BlockHitResult blockHitResult) {
            return blockHitResult.getPos();
        }
        return player.getPos();
    }
}
