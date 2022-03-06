package io.github.llamarama.team.encircled.common.lib.multiblock.predicates;

import io.github.llamarama.team.encircled.api.multiblock.PositionPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;

import java.util.Random;

public record BlockTagPredicate(TagKey<Block> tag) implements PositionPredicate {

    @Override
    public boolean checkPos(World world, BlockPos pos) {
        return world.getBlockState(pos).isIn(this.tag);
    }

    public TagKey<Block> getTag() {
        return tag;
    }

    public BlockState getRandomState(Random random) {
        return Registry.BLOCK.getOrCreateEntryList(this.tag)
                .getRandom(random)
                .map(RegistryEntry::value)
                .orElse(Blocks.AIR)
                .getDefaultState();
    }

}
