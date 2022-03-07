package io.github.llamarama.team.encircled.common.lib.multiblock.predicates;

import io.github.llamarama.team.encircled.api.multiblock.PositionPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BlockStatePredicate implements PositionPredicate {

    private final BlockState state;

    public BlockStatePredicate(BlockState state) {
        this.state = state;
    }

    public BlockState getState() {
        return this.state;
    }

    public BlockState getClientRenderState(Random random) {
        return this.getState();
    }

    @Override
    public boolean checkPos(@NotNull World world, BlockPos pos) {
        return world.getBlockState(pos).equals(this.state);
    }

}
