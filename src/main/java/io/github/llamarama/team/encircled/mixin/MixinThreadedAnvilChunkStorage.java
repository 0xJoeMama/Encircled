package io.github.llamarama.team.encircled.mixin;

import io.github.llamarama.team.encircled.common.event.custom.ChunkWatchEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class MixinThreadedAnvilChunkStorage {

    @Shadow
    @Final
    ServerWorld world;

    @Inject(method = "sendWatchPackets", at = @At("HEAD"))
    private void fireEvent(ServerPlayerEntity player, ChunkPos pos, MutableObject<ChunkDataS2CPacket> packet, boolean oldWithinViewDistance, boolean newWithinViewDistance, CallbackInfo ci) {
        if (player.world == this.world) {
            if (newWithinViewDistance != oldWithinViewDistance) {
                if (newWithinViewDistance) {
                    ChunkWatchEvents.CHUNK_WATCHED_BY_PLAYER.invoker().onChunkWatched(player, pos);
                } else {
                    ChunkWatchEvents.CHUNK_UNWATCHED_BY_PLAYER.invoker().onChunkUnWatched(player, pos);
                }
            }
        }
    }

}
