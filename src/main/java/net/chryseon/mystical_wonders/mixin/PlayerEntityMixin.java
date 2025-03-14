package net.chryseon.mystical_wonders.mixin;

import net.chryseon.mystical_wonders.util.PlayerEntityDataSaver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityDataSaver {
    private final NbtCompound persistentData = new NbtCompound();
    @Override
    public NbtCompound getPersistentData() {
        return persistentData;
    }
}
