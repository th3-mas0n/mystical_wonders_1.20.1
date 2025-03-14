package net.chryseon.mystical_wonders.block.entity.render.model;

import net.chryseon.mystical_wonders.MysticalWonders;
import net.chryseon.mystical_wonders.block.entity.BellEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class BellModel extends GeoModel<BellEntity> {
    private static final Identifier TEXTURE_IDENTIFIER = new Identifier(MysticalWonders.MODID, "textures/block/bell.png");
    private static final Identifier MODEL_IDENTIFIER = new Identifier(MysticalWonders.MODID, "geo/bell.geo.json");
    private static final Identifier ANIMATION_IDENTIFIER = new Identifier(MysticalWonders.MODID, "animations/bell.animation.json");

    @Override
    public Identifier getModelResource(BellEntity object) {
        return MODEL_IDENTIFIER;
    }

    @Override
    public Identifier getTextureResource(BellEntity object) {
        return TEXTURE_IDENTIFIER;
    }

    @Override
    public Identifier getAnimationResource(BellEntity animatable) {
        return ANIMATION_IDENTIFIER;
    }

}