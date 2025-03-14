package net.chryseon.mystical_wonders.mixin;

import net.chryseon.mystical_wonders.util.PlayerEntityDataSaver;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerKillMixin {
    @Inject(method = "onKilledOther", at = @At("HEAD"))
    private void onKillOther(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        NbtCompound data = ((PlayerEntityDataSaver) player).getPersistentData();
        int killCount = data.getInt("solar_kills");
        data.putInt("solar_kills", killCount + 1);
        player.sendMessage(Text.literal("Solar Power: " + (killCount + 1) + "/20").formatted(Formatting.YELLOW), true);
    }
}

