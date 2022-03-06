package io.github.llamarama.team.encircled.common.register;

import com.google.common.collect.ImmutableMap;
import io.github.llamarama.team.encircled.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.encircled.common.block.PillarBlock;
import io.github.llamarama.team.encircled.common.block.*;
import io.github.llamarama.team.encircled.common.util.IdBuilder;
import io.github.llamarama.team.encircled.common.util.ModItemGroup;
import io.github.llamarama.team.encircled.common.util.misc.SettingsSupplier;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public final class ModBlocks {

    private static final Map<String, Block> REGISTRY = new ConcurrentHashMap<>();
    public static final Block DECORATIVE_PACKED_BLOCK = register("decorative_packed_block",
            new Block(copy(Blocks.WHITE_WOOL)));
    public static final Block SCROLL = register("scroll",
            new ScrollBlock(copy(Blocks.WHITE_WOOL)));
    public static final Block MULTIBLOCK_TEST_BLOCK = register("multiblock_test",
            new TestBlock(copy(Blocks.WHITE_WOOL)));
    private static final SettingsSupplier CHALK_PROPS = () ->
            FabricBlockSettings.of(Material.STONE, MapColor.LIGHT_GRAY)
                    .noCollision()
                    .nonOpaque()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.STONE)
                    .luminance((state) -> state.get(ModBlockProperties.CHALK_TYPE).getLightLevel());
    public static final Block CHALK = register("chalk",
            new ChalkBlock(CHALK_PROPS.get()));
    private static final SettingsSupplier TOFAL_PROPS = () ->
            // Some settings to be accessed quickly.
            FabricBlockSettings.of(Material.STONE, MapColor.ORANGE)
                    .luminance(2)
//                    .breakByTool(FabricToolTags.PICKAXES, 2) // TODO: Tag Datagen will solve this
                    .requiresTool()
                    .strength(2.7f);
    // Register Blocks
    public static final Block TOFAL = register("tofal",
            new Block(TOFAL_PROPS.get()));
    public static final Block TOFAL_BRICKS = register("tofal_bricks",
            new Block(copy(TOFAL)));
    public static final Block TOFAL_BRICK_STAIRS = register("tofal_brick_stairs",
            new AccessibleStairsBlock(TOFAL_BRICKS.getDefaultState(), copy(TOFAL_BRICKS)));
    public static final Block TOFAL_TILES = register("tofal_tiles",
            new Block(copy(TOFAL)));
    public static final Block TOFAL_BRICK_SLAB = register("tofal_bricks_slab",
            new SlabBlock(copy(TOFAL)));
    public static final Block TOFAL_PLATE = register("tofal_plate",
            new PlateBlock(copy(TOFAL)));
    private static final SettingsSupplier WITHERED_STONE_PROPS = () ->
            FabricBlockSettings.of(Material.STONE, MapColor.GRAY)
                    .requiresTool()
                    .strength(3.0f);
//                    .breakByTool(FabricToolTags.PICKAXES, 3); // TODO: Tag Datagen will solve this
    public static final Block WITHERED_STONE = register("withered_stone",
            new Block(WITHERED_STONE_PROPS.get()));
    public static final Block POLISHED_WITHERED_STONE = register("polished_withered_stone",
            new Block(copy(WITHERED_STONE)));
    public static final Block CRACKED_WITHERED_STONE_BRICKS = register("cracked_withered_stone_bricks",
            new Block(copy(WITHERED_STONE)));
    public static final Block CHISELED_WITHERED_STONE_BRICKS = register("chiseled_withered_stone_bricks",
            new Block(copy(WITHERED_STONE)));
    public static final Block WITHERED_STONE_PILLAR = register("withered_stone_pillar",
            new PillarBlock(copy(WITHERED_STONE)));
    public static final Block WITHERED_STONE_PLATE = register("withered_stone_plate",
            new PlateBlock(copy(WITHERED_STONE)));
    public static final Block OFFERING_PLATE = register("offering_plate",
            new OfferingPlateBlock(copy(WITHERED_STONE)));
    public static final Block WITHERED_STONE_SLAB = register("withered_stone_slab",
            new SlabBlock(WITHERED_STONE_PROPS.get()));
    public static final Block WITHERED_STONE_BRICK_SLAB = register("withered_stone_brick_slab",
            new SlabBlock(WITHERED_STONE_PROPS.get()));
    public static final Block WITHERED_STONE_BRICKS = register("withered_stone_bricks",
            new Block(WITHERED_STONE_PROPS.get()));
    public static final Block WITHERED_STONE_BRICK_STAIRS = register("withered_stone_brick_stairs",
            new AccessibleStairsBlock(WITHERED_STONE_BRICKS.getDefaultState(), copy(WITHERED_STONE)));


    private ModBlocks() {
    }

    @NotNull
    private static Block register(String id, Block block) {
        Registry.register(Registry.ITEM, IdBuilder.of(id), new BlockItem(block,
                new Item.Settings().group(ModItemGroup.get())));
        return registerNoItem(id, block);
    }

    @NotNull
    private static Block registerNoItem(String id, @NotNull Block block) {
        REGISTRY.putIfAbsent(id, block);
        return block;
    }

    private static AbstractBlock.Settings copy(AbstractBlock block) {
        return FabricBlockSettings.copyOf(block);
    }

    public static ImmutableMap<String, Block> getModBlocks() {
        return ImmutableMap.copyOf(REGISTRY);
    }

    public static void init() {
        REGISTRY.forEach((id, block) -> Registry.register(Registry.BLOCK, IdBuilder.of(id), block));
    }

}
