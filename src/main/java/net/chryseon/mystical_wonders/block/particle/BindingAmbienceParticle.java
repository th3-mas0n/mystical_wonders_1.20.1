package net.chryseon.mystical_wonders.block.particle;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;


import java.util.Random;


public class BindingAmbienceParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    private final float redEvolution;
    private final float greenEvolution;
    private final float blueEvolution;


    private BindingAmbienceParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BindingAmbienceParticleEffect bindingParticleEffect, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.alpha = bindingParticleEffect.getAlpha();
        this.red = bindingParticleEffect.getRed();
        this.green = bindingParticleEffect.getGreen();
        this.blue = bindingParticleEffect.getBlue();
        this.redEvolution = bindingParticleEffect.getRedEvolution();
        this.greenEvolution = bindingParticleEffect.getGreenEvolution();
        this.blueEvolution = bindingParticleEffect.getBlueEvolution();
        this.maxAge = 70 + this.random.nextInt(10);
        this.scale *= 0.75f + new Random().nextFloat() * 0.50f;
        this.spriteProvider = spriteProvider;
        this.setSprite(spriteProvider.getSprite(0, 2));
        this.velocityY = 0f;
    }


    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }


    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        // fade and die
        if (this.age++ >= this.maxAge) {
            alpha -= 0.004f;
        }
        if (this.age++ <= 30) {
            this.alpha = Math.max(0, this.alpha + 0.03f);
        } else {
            this.alpha = Math.max(0, this.alpha - 0.001f);
        }
        if (alpha < 0f || this.scale <= 0f) {
            this.markDead();
        }
        red = MathHelper.clamp(red + redEvolution, 0, 1);
        green = MathHelper.clamp(green + greenEvolution, 0, 1);
        blue = MathHelper.clamp(blue + blueEvolution, 0, 1);
        this.velocityY -= 0.0001;
        this.velocityX = 0;
        this.velocityZ = 0;
        this.move(velocityX, velocityY, velocityZ);
    }


    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        Quaternionf quaternion2;
        if (this.angle == 0.0F) {
            quaternion2 = camera.getRotation();
        } else {
            quaternion2 = new Quaternionf(camera.getRotation());
            float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
            quaternion2.rotateZ(i);
        }
        Vector3f vector3f = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f.rotate(quaternion2);
        Vector3f[] vector3fs = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };
        float j = this.getSize(tickDelta);
        for (int k = 0; k < 4; ++k) {
            Vector3f vector3f2 = vector3fs[k];
            vector3f2.rotate(quaternion2);
            vector3f2.mul(j);
            vector3f2.add(f, g, h);
        }
        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int l = 15728880;
        vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(maxU, maxV).color(red, green, blue, alpha).light(l).next();
        vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(maxU, minV).color(red, green, blue, alpha).light(l).next();
        vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(minU, minV).color(red, green, blue, alpha).light(l).next();
        vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(minU, maxV).color(red, green, blue, alpha).light(l).next();
    }


    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<BindingAmbienceParticleEffect> {
        private final SpriteProvider spriteProvider;


        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }


        public Particle createParticle(BindingAmbienceParticleEffect bindingParticleEffect, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new BindingAmbienceParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, bindingParticleEffect, this.spriteProvider);
        }
    }
}