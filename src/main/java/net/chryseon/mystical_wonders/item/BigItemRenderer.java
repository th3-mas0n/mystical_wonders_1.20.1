package net.chryseon.mystical_wonders.item;


import com.mojang.datafixers.util.Unit;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.joml.Quaternionf;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class BigItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, IdentifiableResourceReloadListener {
    private final Identifier id;
    private final Identifier itemId;
    private ItemRenderer itemRenderer;
    private BakedModel inventoryModel;
    private BakedModel worldModel;


    public BigItemRenderer(Identifier id) {
        this.id = new Identifier(id.getNamespace(), id.getPath() + "_renderer");
        this.itemId = id;
    }


    @Override
    public Identifier getFabricId() {
        return this.id;
    }


    @Override
    public CompletableFuture<Void> reload(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        return synchronizer.whenPrepared(Unit.INSTANCE).thenRunAsync(() -> {
            applyProfiler.startTick();
            applyProfiler.push("listener");
            final MinecraftClient client = MinecraftClient.getInstance();
            this.itemRenderer = client.getItemRenderer();
            this.inventoryModel = client.getBakedModelManager().getModel(new ModelIdentifier(new Identifier(itemId.getNamespace(), itemId.getPath() + "_gui"), "inventory"));
            this.worldModel = client.getBakedModelManager().getModel(new ModelIdentifier(new Identifier(itemId.getNamespace(), itemId.getPath() + "_handheld"), "inventory"));
            applyProfiler.pop();
            applyProfiler.endTick();
        }, applyExecutor);
    }


    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.pop();
        matrices.push();
        if(itemRenderer != null) {
            if (mode != ModelTransformationMode.FIRST_PERSON_LEFT_HAND &&
                    mode != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND &&
                    mode != ModelTransformationMode.THIRD_PERSON_LEFT_HAND &&
                    mode != ModelTransformationMode.THIRD_PERSON_RIGHT_HAND &&
                    mode != ModelTransformationMode.NONE) {
                itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, this.inventoryModel);
            } else {
                boolean leftHanded = switch (mode) {
                    case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND -> true;
                    default -> false;
                };
                if (MinecraftClient.getInstance().player.getActiveItem() == stack) {
                    if (mode.isFirstPerson()) {
                        matrices.multiply(new Quaternionf().rotationX((float) Math.toRadians(-45)));
                        matrices.translate(0.15, -0.25, 0);
                    } else {
                        matrices.multiply(new Quaternionf().rotationX((float) Math.toRadians(180)));
                    }
                }
                itemRenderer.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, this.worldModel);
            }
        }
    }
}