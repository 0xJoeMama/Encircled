package io.github.llamarama.team.encircled.common.lib.chaos;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import io.github.llamarama.team.encircled.Encircled;
import io.github.llamarama.team.encircled.common.event.EventHandler;
import io.github.llamarama.team.encircled.common.network.ModNetworking;
import io.github.llamarama.team.encircled.common.network.packet.MassChunkUpdatePacket;
import io.github.llamarama.team.encircled.common.util.ChaosUtils;
import io.github.llamarama.team.encircled.common.util.constants.IntegerConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Used to synchonize chunk data from server, which manages all the logic, to client.
 *
 * @author 0xJoeMama
 */
public class ChunkSyncManager {

    /**
     * The way to access an instance of this class.
     * Keep in mind this instance is initialized at server start and removed at server stop.
     */
    public static ChunkSyncManager INSTANCE;
    /**
     * The positions of each {@link Chunk} a player loads and is not synced.
     * This map is emptied once that player receives a {@link MassChunkUpdatePacket}.
     *
     * @see ChunkSyncManager#sendStatus(ServerPlayerEntity)
     */
    private final SetMultimap<UUID, ChunkPos> pending;

    /**
     * Only called by {@link EventHandler}.
     */
    public ChunkSyncManager() {
        // We use sets because we never need to worry about specific indices for values.
        this.pending = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new);

        // This will remain so that players can debug whether the manager exists.
        Encircled.getLogger().info("Created chunk sync manager for server.");
    }

    /**
     * Add a new {@link ChunkPos} to the given {@link PlayerEntity} so that in the next pop event, it will be updated.
     *
     * @param chunkPos The position of the new chunk.
     * @param player   The target player.
     */
    public void push(ChunkPos chunkPos, PlayerEntity player) {
        if (this.pending.containsEntry(player.getUuid(), chunkPos))
            return;

        UUID uniqueID = player.getUuid();
        this.pending.put(uniqueID, chunkPos);

        Encircled.getLogger().debug("Pushed chunk at " + chunkPos);
    }

    /**
     * @param player   The player to pop.
     * @param chunkPos The position of the target chunk.
     * @see ChunkSyncManager#pop(UUID, ChunkPos)
     */
    public void pop(PlayerEntity player, ChunkPos chunkPos) {
        this.pop(player.getUuid(), chunkPos);
    }

    /**
     * Removes a chunk from the target {@link PlayerEntity}.
     *
     * @param uuid The uuid of the player entity.
     * @param pos  The position of the chunk.
     */
    public void pop(UUID uuid, ChunkPos pos) {
        // This may not be needed but I do it anyway.
        if (this.pending.containsEntry(uuid, pos))
            this.pending.remove(uuid, pos);
    }

    /**
     * Completely wipes all the chunks from a target player.
     * Currently used by {@link io.github.llamarama.team.encircled.common.event.EventHandler}.
     *
     * @param uuid The uuid of the target player.
     */
    public void popKey(UUID uuid) {
        // This may not be needed but I do it anyway.
        if (this.pending.containsKey(uuid))
            this.pending.removeAll(uuid);
    }

    /**
     * Used by {@link EventHandler}.
     * Clears the whole cache map.
     */
    public void popAll() {
        this.pending.clear();
    }

    /**
     * Sends the new chunk status to the player, while removing all of the chunks that were updated.
     * {@link EventHandler}.
     *
     * @param player The target player.
     * @see ChunkSyncManager#enqueue
     */
    public void sendStatus(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        /*
            This map contains the new chaos values as well as the positions of the chunks that need updating.
            It is sent through the network and when received the client updates its world view.
            Currently it only works for chaos values but it may be possible to get it to work with other values as well.
            However there is no need to do that at the moment.
         */
        HashMap<ChunkPos, Integer> toBSent = new HashMap<>();

        // Check if the player has an entry assigned to him.
        if (!this.pending.containsKey(player.getUuid())) return;

        // Get the chaos values and write them to the map.
        Set<ChunkPos> chunksToUpdate = this.pending.removeAll(player.getUuid());
        chunksToUpdate.forEach((chunkPos) -> {
            Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
            ChaosUtils.executeForChaos(chunk, chaosProvider -> toBSent.put(chunkPos, chaosProvider.getChaosValue()));
        });

        // Send the packet.
        ModNetworking.get().sendToClient(new MassChunkUpdatePacket(toBSent), player);

        // Log the fact that a packet was sent in debug mode.
        Encircled.getLogger().debug("Status sent to " + player);
    }

    /**
     * Used to send a packet to all players so that they get the updated chunk info.
     * Currently used by {@link EventHandler}
     *
     * @param runnable The packet send call.
     */
    public void enqueue(Runnable runnable) {
        // The number we compare may need tweaking to get it to work well and efficiently for both single player and
        // multiplayer. Currently it is set to the minimum amount of chunks a client can render at once.
        if (this.pending.size() > IntegerConstants.MIN_RENDER_DISTANCE) {
            runnable.run();
        }
    }

}
