package io.github.llamarama.team.encircled.mixin;

import io.github.llamarama.team.encircled.common.lib.chaos.ChaosProvider;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Implements(@Interface(iface = ChaosProvider.class, prefix = "vm$"))
@Mixin(WorldChunk.class)
public abstract class MixinWorldChunk {

    private int vm$chaos = 69420;

    @Shadow
    public abstract void markDirty();

    public int vm$getChaosValue() {
        return this.vm$chaos;
    }

    public void vm$setChaosValue(int newVal) {
        this.vm$chaos = newVal;
        this.markDirty();
    }

}
