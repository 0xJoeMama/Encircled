package io.github.llamarama.team.encircled.common.lib.multiblock.predicates;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockPredicate extends BlockStatePredicate {

    private final Block block;

    public BlockPredicate(@NotNull Block block) {
        super(block.getDefaultState());
        this.block = block;
    }

    public Block getBlock() {
        return this.block;
    }

    @Override
    public boolean checkPos(@NotNull World world, BlockPos pos) {
        return world.getBlockState(pos).isOf(this.block);
    }

}
