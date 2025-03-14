package net.chryseon.mystical_wonders;

import com.mojang.serialization.Codec;
import net.chryseon.mystical_wonders.block.entity.render.BellRender;
import net.chryseon.mystical_wonders.block.particle.BindingAmbienceParticle;
import net.chryseon.mystical_wonders.block.particle.BindingAmbienceParticleEffect;
import net.chryseon.mystical_wonders.item.BigItemRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemConvertible;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class MysticalWondersClient implements ClientModInitializer {
    public static ParticleType<BindingAmbienceParticleEffect> BINDING;

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(MysticalWonders.BELL_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new BellRender());
        registerBigRenderer(MysticalWonders.STAR_STAFF);
//        registerBigRenderer(MysticalWonders.DAWNBREAKER);
        BINDING = Registry.register(Registries.PARTICLE_TYPE, "mystical_wonders:bound", new ParticleType<BindingAmbienceParticleEffect>(true, BindingAmbienceParticleEffect.PARAMETERS_FACTORY) {
            @Override
            public Codec<BindingAmbienceParticleEffect> getCodec() {
                return BindingAmbienceParticleEffect.CODEC;
            }
        });
        ParticleFactoryRegistry.getInstance().register(BINDING, BindingAmbienceParticle.Factory::new);
        ClientTickEvents.END_WORLD_TICK.register(world -> {
        });
    }
    private void registerBigRenderer(ItemConvertible item) {
        Identifier bigId = Registries.ITEM.getId(item.asItem());
        BigItemRenderer bigItemRenderer = new BigItemRenderer(bigId);
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(bigItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(item, bigItemRenderer);
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelIdentifier(new Identifier(bigId.getNamespace(), bigId.getPath() + "_gui"), "inventory"));
            out.accept(new ModelIdentifier(new Identifier(bigId.getNamespace(), bigId.getPath() + "_handheld"), "inventory"));
        });
    }

}
