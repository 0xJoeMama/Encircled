package io.github.llamarama.team.encircled.mixin;

import io.github.llamarama.team.encircled.common.lib.chaos.ChaosProvider;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.BlendingData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Implements(@Interface(iface = ChaosProvider.class, prefix = "vm$"))
@Mixin(WorldChunk.class)
public abstract class MixinWorldChunk extends Chunk {

    private int vm$chaos = 69420;

    public MixinWorldChunk(ChunkPos pos, UpgradeData upgradeData, HeightLimitView heightLimitView, Registry<Biome> biome, long inhabitedTime, @Nullable ChunkSection[] sectionArrayInitializer, @Nullable BlendingData blendingData) {
        super(pos, upgradeData, heightLimitView, biome, inhabitedTime, sectionArrayInitializer, blendingData);
    }

    public int vm$getChaosValue() {
        return this.vm$chaos;
    }

    public void vm$setChaosValue(int newVal) {
        this.vm$chaos = newVal;
        this.setNeedsSaving(true);
    }

}