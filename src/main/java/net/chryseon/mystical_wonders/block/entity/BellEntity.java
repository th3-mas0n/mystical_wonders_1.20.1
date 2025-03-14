package net.chryseon.mystical_wonders.block.entity;

import net.chryseon.mystical_wonders.MysticalWonders;
import net.chryseon.mystical_wonders.block.particle.BindingAmbienceParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.InstancedAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.HashSet;
import java.util.Set;

public class BellEntity extends BlockEntity implements GeoAnimatable {
    private final AnimatableInstanceCache factory = new InstancedAnimatableInstanceCache(this);
    public static final RawAnimation IDLE = RawAnimation.begin().thenPlay("animation.model.idle");
    public static final RawAnimation RING = RawAnimation.begin().thenPlay("animation.model.ring");
    public boolean isRinging = false;
    public boolean isActive = false;
    private static final int BARRIER_RADIUS = 200;
    public static final Set<BlockPos> ACTIVE_BELLS = new HashSet<>();


    public void toggleBarrier() {
        isActive = !isActive;
        if (isActive) {
            ACTIVE_BELLS.add(getPos());
            spawnBarrierParticles();
        } else {
            ACTIVE_BELLS.remove(getPos());
        }
    }

    private void spawnBarrierParticles() {
        if (world == null) return;

        BlockPos pos = getPos();
        Random random = world.getRandom();

        // Generate the walls, floor, and ceiling
        for (int x = -BARRIER_RADIUS; x <= BARRIER_RADIUS; x++) {
            for (int y = -BARRIER_RADIUS; y <= BARRIER_RADIUS; y++) {
                for (int z = -BARRIER_RADIUS; z <= BARRIER_RADIUS; z++) {
                    if (Math.abs(x) == BARRIER_RADIUS || Math.abs(y) == BARRIER_RADIUS || Math.abs(z) == BARRIER_RADIUS) {
                        double px = pos.getX() + x + (random.nextDouble() - 0.5);
                        double py = pos.getY() + y + (random.nextDouble() - 0.5);
                        double pz = pos.getZ() + z + (random.nextDouble() - 0.5);
                        world.addParticle(new BindingAmbienceParticleEffect(1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f),
                                px, py, pz, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
    }

    public BellEntity(BlockPos pos, BlockState state) {
        super(MysticalWonders.BELL_ENTITY, pos, state);
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar animationData) {
        animationData.add(new AnimationController<>(this, "controller", 2, animationEvent -> {
            if (isRinging) {
                animationEvent.getController().setAnimation(RING);
                isRinging = false; // Reset after playing
            } else {
                animationEvent.getController().setAnimation(IDLE);
            }
            return PlayState.CONTINUE;
        }));

        animationData.add(new AnimationController<>(this, "controller", 2, animationEvent -> {
            RawAnimation anime = IDLE;
            animationEvent.getController().setAnimation(anime);
            return PlayState.CONTINUE;
        }));

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }

}