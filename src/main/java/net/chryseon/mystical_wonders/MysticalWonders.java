package net.chryseon.mystical_wonders;


import net.chryseon.mystical_wonders.block.entity.BellEntity;
import net.chryseon.mystical_wonders.item.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.GeckoLib;


import java.util.HashMap;
import java.util.UUID;
import java.util.function.ToIntFunction;


public class MysticalWonders implements ModInitializer {
    public static final HashMap<UUID, Boolean> ACTIVE_PLAYERS = new HashMap<>();
    public static final HashMap<UUID, Integer> BARRIER_TICKS = new HashMap<>();

    public static String MODID = "mystical_wonders";
    public static final Item CONTRACT = new ContractItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE));
    public static final Item TELEPORTER = new TeleporterItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE));
    public static final Item HANDBELL = new HandbellItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE));
    public static final Item MARK_OF_MERCHANT = new MarkOfMerchantItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE));
//    public static final Item SEVERANCE_OF_FATE = new DawnBrakerItem(ToolMaterials.NETHERITE, 5, -3.0F, new Item.Settings().maxCount(1).rarity(Rarity.RARE));
    public static final Item STAR_STAFF = new StarStaffItem(ToolMaterials.NETHERITE, 4, -3.0F, new Item.Settings().maxCount(1).rarity(Rarity.RARE));


//    public static final Block BELL = new Block(FabricBlockSettings.copyOf(Blocks.CHISELED_DEEPSLATE).requiresTool().luminance(10).ticksRandomly());
//    public static BlockEntityType<BellBlockEntity> BELL_ENTITY;

    public static final Block BELL_BLOCK = new BellBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).requiresTool().luminance(0).ticksRandomly());
    public static BlockEntityType<BellEntity> BELL_ENTITY;
    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier(MODID, "contract"), CONTRACT);
        Registry.register(Registries.ITEM, new Identifier(MODID, "mark_of_merchant"), MARK_OF_MERCHANT);
        Registry.register(Registries.ITEM, new Identifier(MODID, "handbell"), HANDBELL);
        Registry.register(Registries.ITEM, new Identifier(MODID, "star_staff"), STAR_STAFF);
        Registry.register(Registries.ITEM, new Identifier(MODID, "teleporter"), TELEPORTER);

//        Registry.register(Registries.ITEM, new Identifier(MODID, "bell"), new BlockItem(BELL, new FabricItemSettings()));
//        Registry.register(Registries.BLOCK, new Identifier(MODID, "bell"), BELL);

//        BELL_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "bell"),
//                FabricBlockEntityTypeBuilder.create(BellBlockEntity::new, BELL).build(null));

        GeckoLib.initialize();


        ServerTickEvents.START_WORLD_TICK.register(world -> {
            for (PlayerEntity player : world.getPlayers()) {
                UUID playerId = player.getUuid();

                // Check if player has an active barrier
                if (ACTIVE_PLAYERS.getOrDefault(playerId, false)) {
                    BlockPos pos = player.getBlockPos();
                    HandbellItem.spawnBarrier(world, pos);

                    // Decrease the barrier timer
                    int remainingTicks = BARRIER_TICKS.getOrDefault(playerId, 0);

                    if (remainingTicks <= 0) {
                        HandbellItem.removeBarrier(world, pos);
                        ACTIVE_PLAYERS.put(playerId, false);
                        BARRIER_TICKS.remove(playerId);
                        player.sendMessage(Text.of("Barrier deactivated!"), true);
                    } else {
                        BARRIER_TICKS.put(playerId, remainingTicks - 1);
                    }
                }
            }
        });

//        ELL_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "bell"),
//                FabricBlockEntityTypeBuilder.create(BellEntity::new, BELL_BLOCK).build(null));
//        Registry.register(Registries.ITEM, new Identifier(MODID, "bell"), new BlockItem(BELL_BLOCK, new FabricItemSettings()));
//        Registry.register(Registries.BLOCK, new Identifier(MODID, "bell"), BELL_BLOCK);

        BELL_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "bell"),
                FabricBlockEntityTypeBuilder.create(BellEntity::new, BELL_BLOCK).build(null));
        Registry.register(Registries.ITEM, new Identifier(MODID, "bell"), new BlockItem(BELL_BLOCK, new FabricItemSettings()));
        Registry.register(Registries.BLOCK, new Identifier(MODID, "bell"), BELL_BLOCK);

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.getBlockEntity(hitResult.getBlockPos()) instanceof BellEntity bellEntity) {
                bellEntity.isRinging = true;
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
    }




    public static void activateBarrier(PlayerEntity player, int durationInTicks) {
        UUID playerId = player.getUuid();

        // Activate the barrier
        ACTIVE_PLAYERS.put(playerId, true);
        BARRIER_TICKS.put(playerId, durationInTicks);
    }
//
//    public static void activateBarrier(PlayerEntity player, int durationInTicks) {
//        UUID playerId = player.getUuid();
//
//        // Activate the barrier
//        ACTIVE_PLAYERS.put(playerId, true);
//        BARRIER_TICKS.put(playerId, durationInTicks);
//    }
//
//    public static void scheduleBarrierDeactivation(PlayerEntity player, int ticks) {
//        player.getServer().getScheduler().schedule(() -> {
//            ACTIVE_PLAYERS.put(player.getUuid(), false);
//            HandbellItem.removeBarrier(player.getWorld(), player.getBlockPos());
//            player.sendMessage(Text.of("Barrier deactivated!"), true);
//        }, ticks);
//    }


    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return (state) -> (Boolean) state.get(Properties.LIT) ? litLevel : 0;
    }

    private static ToIntFunction<BlockState> createLightLevelFromPoweredBlockState(int litLevel) {
        return (state) -> (Boolean) state.get(Properties.POWERED) ? litLevel : 0;
    }


}
