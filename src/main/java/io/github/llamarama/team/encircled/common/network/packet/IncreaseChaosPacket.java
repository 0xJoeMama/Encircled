package io.github.llamarama.team.encircled.common.network.packet;

import io.github.llamarama.team.encircled.common.network.ModNetworking;
import io.github.llamarama.team.encircled.common.util.ChaosUtils;
import io.github.llamarama.team.encircled.common.util.IdBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

public class IncreaseChaosPacket extends DefaultC2SPacket {

    public static final Identifier INCREASE_CHAOS_PACKET_ID = IdBuilder.of("increase_chaos");

    public IncreaseChaosPacket(ChunkPos chunkPos, int amount) {
        super(INCREASE_CHAOS_PACKET_ID, PacketByteBufs.create());
        PacketByteBuf buffer = this.getBuffer();

        buffer.writeInt(amount);
        buffer.writeChunkPos(chunkPos);
    }

    public IncreaseChaosPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        super(INCREASE_CHAOS_PACKET_ID, server, player, handler, buf, responseSender);
        int amount = buf.readInt();
        ChunkPos chunkPos = buf.readChunkPos();
        ServerWorld world = player.getWorld();
        WorldChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);

        server.execute(() -> ChaosUtils.executeForChaos(chunk, chaosProvider -> {
            chaosProvider.increase(amount);
            ModNetworking.get().sendToAllClose(new ChunkChaosUpdatePacket(chunk), world, chunk.getPos().getStartPos());
        }));
    }

}
