package net.chryseon.mystical_wonders.block.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.chryseon.mystical_wonders.MysticalWondersClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import java.util.Locale;

public class BindingAmbienceParticleEffect implements ParticleEffect {
    public static final Codec<BindingAmbienceParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.FLOAT.fieldOf("r").forGetter((bindParticleEffect) -> bindParticleEffect.red), Codec.FLOAT.fieldOf("g").forGetter((bindParticleEffect) -> bindParticleEffect.green), Codec.FLOAT.fieldOf("b").forGetter((bindParticleEffect) -> bindParticleEffect.blue), Codec.FLOAT.fieldOf("re").forGetter((bindParticleEffect) -> bindParticleEffect.redEvolution), Codec.FLOAT.fieldOf("ge").forGetter((bindParticleEffect) -> bindParticleEffect.greenEvolution), Codec.FLOAT.fieldOf("be").forGetter((bindParticleEffect) -> bindParticleEffect.blueEvolution), Codec.FLOAT.fieldOf("a").forGetter((bindParticleEffect) -> bindParticleEffect.alpha)).apply(instance, BindingAmbienceParticleEffect::new));
    public static final ParticleEffect.Factory<BindingAmbienceParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<>() {
        public BindingAmbienceParticleEffect read(ParticleType<BindingAmbienceParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float r = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float g = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float b = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float re = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float ge = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float be = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float a = (float) stringReader.readDouble();
            return new BindingAmbienceParticleEffect(r, g, b, re, ge, be, a);
        }

        public BindingAmbienceParticleEffect read(ParticleType<BindingAmbienceParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new BindingAmbienceParticleEffect(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat());
        }
    };
    private final float red;
    private final float green;
    private final float blue;
    private final float redEvolution;
    private final float greenEvolution;
    private final float blueEvolution;
    private final float alpha;


    public BindingAmbienceParticleEffect(float red, float green, float blue, float redEvolution, float greenEvolution, float blueEvolution, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.redEvolution = redEvolution;
        this.greenEvolution = greenEvolution;
        this.blueEvolution = blueEvolution;
        this.alpha = alpha;
    }

    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.red);
        buf.writeFloat(this.green);
        buf.writeFloat(this.blue);
        buf.writeFloat(this.redEvolution);
        buf.writeFloat(this.greenEvolution);
        buf.writeFloat(this.blueEvolution);
        buf.writeFloat(this.alpha);
    }

    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f", Registries.PARTICLE_TYPE.getId(this.getType()), this.red, this.green, this.blue, this.redEvolution, this.greenEvolution, this.blueEvolution, this.alpha);
    }

    public ParticleType<BindingAmbienceParticleEffect> getType() {
        return MysticalWondersClient.BINDING;
    }

    @Environment(EnvType.CLIENT)
    public float getRed() {
        return this.red;
    }

    @Environment(EnvType.CLIENT)
    public float getGreen() {
        return this.green;
    }

    @Environment(EnvType.CLIENT)
    public float getBlue() {
        return this.blue;
    }

    @Environment(EnvType.CLIENT)
    public float getRedEvolution() {
        return redEvolution;
    }

    @Environment(EnvType.CLIENT)
    public float getGreenEvolution() {
        return greenEvolution;
    }

    @Environment(EnvType.CLIENT)
    public float getBlueEvolution() {
        return blueEvolution;
    }

    @Environment(EnvType.CLIENT)
    public float getAlpha() {
        return alpha;
    }
}