package io.github.llamarama.team.encircled.api.spellbinding;

import io.github.llamarama.team.encircled.api.multiblock.MultiblockType;
import io.github.llamarama.team.encircled.common.lib.multiblock.impl.CircleMultiblock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public interface SpellbindingCircle {

    Supplier<? extends CircleMultiblock> multiblock(BlockPos center, World world);

    Spellbindable getResult();

    int getCastingTime();

    MultiblockType getMultiblockType();

}
