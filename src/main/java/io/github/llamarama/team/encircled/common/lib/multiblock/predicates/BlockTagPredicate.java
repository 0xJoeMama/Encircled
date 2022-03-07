package io.github.llamarama.team.encircled.common.lib.multiblock.predicates;

import io.github.llamarama.team.encircled.api.multiblock.PositionPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class BlockTagPredicate extends BlockStatePredicate implements PositionPredicate {

    private final TagKey<Block> tag;
    private final AtomicReference<Optional<BlockState>> cachedState;
    private int cachedTime;

    public BlockTagPredicate(TagKey<Block> tag) {
        super(Blocks.AIR.getDefaultState());
        this.tag = tag;
        this.cachedState = new AtomicReference<>(Optional.empty());
        this.cachedTime = 0;
    }

    @Override
    public boolean checkPos(@NotNull World world, BlockPos pos) {
        return world.getBlockState(pos).isIn(this.tag);
    }

    @Override
    public BlockState getState() {
        return Registry.BLOCK.getOrCreateEntryList(this.tag)
                .stream()
                .findAny()
                .map(RegistryEntry::value)
                .orElse(Blocks.AIR)
                .getDefaultState();
    }

    @Override
    public BlockState getClientRenderState(Random random) {
        if (this.cachedTime >= 180 || this.cachedState.get().isEmpty()) {
            this.cachedTime = 0;
            this.cachedState.set(Optional.of(this.getRandomState(random)));
            return this.cachedState.get().get();
        }

        this.cachedTime++;
        return this.cachedState.get().get();
    }

    public TagKey<Block> getTag() {
        return this.tag;
    }

    public BlockState getRandomState(Random random) {
        return Registry.BLOCK.getOrCreateEntryList(this.tag)
                .getRandom(random)
                .map(RegistryEntry::value)
                .orElse(Blocks.AIR)
                .getDefaultState();
    }

}
