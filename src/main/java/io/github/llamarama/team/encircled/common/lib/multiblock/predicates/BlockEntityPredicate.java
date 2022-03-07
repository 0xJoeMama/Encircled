package io.github.llamarama.team.encircled.common.lib.multiblock.predicates;

import io.github.llamarama.team.encircled.api.multiblock.PositionPredicate;
import io.github.llamarama.team.encircled.mixin.AccessorBlockEntityType;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockEntityPredicate extends BlockStatePredicate implements PositionPredicate {

    private final BlockEntityType<?> type;

    public BlockEntityPredicate(BlockEntityType<?> type) {
        super(((AccessorBlockEntityType) type).getBlocks().stream().findAny().orElse(Blocks.AIR).getDefaultState());
        this.type = type;
    }

    @Override
    public boolean checkPos(@NotNull World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        return blockEntity != null && blockEntity.getType() == this.type;
    }

    public BlockEntityType<?> getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BlockEntityPredicate) obj;
        return Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "BlockEntityPredicate[" +
                "type=" + type + ']';
    }


}
