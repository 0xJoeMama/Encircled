package io.llamarama.team.voidmagic.common.register;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.block.TofalBlock;
import io.llamarama.team.voidmagic.common.block.WitheredStoneBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class ModBlocks {

    public static final RegistryObject<WitheredStoneBlock> WITHERED_STONE = register("withered_stone",
            () -> new WitheredStoneBlock(getWitheredStoneProperties()));
    public static final RegistryObject<WitheredStoneBlock> WITHERED_STONE_BRICKS = register("withered_stone_bricks",
            () -> new WitheredStoneBlock(copyProperties(WITHERED_STONE.get())));
    public static final RegistryObject<WitheredStoneBlock> CHISELED_WITHERED_STONE_BRICKS = register("chiseled_withered_stone_bricks",
            () -> new WitheredStoneBlock(getWitheredStoneProperties()));
    public static final RegistryObject<WitheredStoneBlock> CRACKED_WITHER_STONE_BRICKS = register("cracked_withered_stone_bricks",
            () -> new WitheredStoneBlock(getWitheredStoneProperties()));
    public static final RegistryObject<TofalBlock> TOFAL = register("tofal",
            () -> new TofalBlock(getTofalProperties()));
    public static final RegistryObject<TofalBlock> TOFAL_BRICKS = register("tofal_bricks",
            () -> new TofalBlock(getTofalProperties()));
    public static final RegistryObject<TofalBlock> TOFAL_TILES = register("tofal_tiles",
            () -> new TofalBlock(getTofalProperties()));
    public static final RegistryObject<Block> SHADOW_BRICKS = register("shadow_bricks",
            () -> new Block(getTofalProperties().lightLevel((state) -> 0)));
    public static final RegistryObject<OreBlock> END_PUTILIAM_ORE = register("end_putiliam_ore",
            () -> new OreBlock(copyProperties(Blocks.END_STONE)));
    public static final RegistryObject<OreBlock> OVERWORLD_PUTILIAM_ORE = register("overworld_putiliam_ore",
            () -> new OreBlock(copyProperties(Blocks.IRON_ORE)));

    private ModBlocks() {
    }

    @NotNull
    private static <B extends Block> RegistryObject<B> register(String id, Supplier<B> block) {
        RegistryObject<B> out = registerNoItem(id, block);
        ModRegistries.ITEMS.register(out.getId().getPath(), () -> new BlockItem(out.get(), new Item.Properties().tab(ItemGroup.TAB_MISC).tab(VoidMagic.GROUP)));
        return out;
    }

    @NotNull
    private static <B extends Block> RegistryObject<B> registerNoItem(String id, Supplier<B> block) {
        return ModRegistries.BLOCKS.register(id, block);
    }

    public static void init(IEventBus bus) {
        ModRegistries.BLOCKS.register(bus);
    }

    @NotNull
    private static AbstractBlock.Properties getWitheredStoneProperties() {
        return AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).harvestLevel(3).harvestTool(ToolType.PICKAXE).strength(3.0f).requiresCorrectToolForDrops();
    }

    @NotNull
    private static AbstractBlock.Properties getTofalProperties() {
        return AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).strength(3.0f).harvestTool(ToolType.PICKAXE).harvestLevel(2).lightLevel((state) -> 7).requiresCorrectToolForDrops();
    }

    @Nullable
    private static AbstractBlock.Properties copyProperties(AbstractBlock block) {
        return block == null ? null : AbstractBlock.Properties.copy(block);
    }

}
