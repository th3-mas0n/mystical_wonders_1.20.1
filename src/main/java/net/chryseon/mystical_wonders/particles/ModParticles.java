//package net.chryseon.mystical_wonders.particles;
//
//import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
//import net.minecraft.particle.DefaultParticleType;
//import net.minecraft.registry.Registries;
//import net.minecraft.registry.Registry;
//import net.minecraft.util.Identifier;
//
//public class ModParticles {
//    public static final DefaultParticleType GOLDEN_CIRCLE = Registry.register(
//            Registries.PARTICLE_TYPE,
//            new Identifier("mystical_wonders", "golden_circle"),
//            new DefaultParticleType(true) { }
//    );
//
//    public static void register() {
//        ParticleFactoryRegistry.getInstance().register(GOLDEN_CIRCLE, GoldenCircleParticle.Factory::new);
//    }
//}
