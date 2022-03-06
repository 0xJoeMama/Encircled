package io.github.llamarama.team.encircled.api.multiblock;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Multiblock {

    MultiblockType getType();

    BlockPos getPos();

    MultiblockRotation getRotation();

    default boolean exists(World world) {
        return this.getType().existsAt(this.getPos(), world);
    }

    @NotNull
    Collection<BlockPos> positions();

    void writeNbt(NbtCompound nbt);

    void readNbt(NbtCompound nbt);

}
