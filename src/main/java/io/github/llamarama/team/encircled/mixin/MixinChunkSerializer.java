package io.github.llamarama.team.encircled.mixin;

import io.github.llamarama.team.encircled.common.util.ChaosUtils;
import io.github.llamarama.team.encircled.common.util.constants.NBTConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.ReadOnlyChunk;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public abstract class MixinChunkSerializer {

    @Inject(method = "serialize", at = @At("RETURN"), cancellable = true)
    private static void serializeCustomData(ServerWorld world, Chunk chunk, CallbackInfoReturnable<NbtCompound> cir) {
        NbtCompound voidMagicExtraData = new NbtCompound();

        voidMagicExtraData.putString(NBTConstants.VOIDMAGIC_VERSION_KEY, NBTConstants.VOIDMAGIC_VERSION);
        ChaosUtils.executeForChaos(chunk,
                chaosProvider -> voidMagicExtraData.putInt(NBTConstants.CHAOS, chaosProvider.getChaosValue()));

        NbtCompound returnValue = cir.getReturnValue();
        returnValue.put(NBTConstants.VOIDMAGIC_DATA, voidMagicExtraData);

        cir.setReturnValue(returnValue);
    }

    @Inject(method = "deserialize", at = @At("RETURN"), cancellable = true)
    private static void deserializeCustomData(ServerWorld world, StructureManager structureManager, PointOfInterestStorage poiStorage, ChunkPos pos, NbtCompound nbt, CallbackInfoReturnable<ProtoChunk> cir) {
        ProtoChunk chunk = cir.getReturnValue();

        if (chunk instanceof ReadOnlyChunk readOnly) {
            ChaosUtils.executeForChaos(readOnly.getWrappedChunk(), chaosProvider ->
                    chaosProvider.setChaosValue(nbt.getCompound(NBTConstants.VOIDMAGIC_DATA).getInt(NBTConstants.CHAOS))
            );
        }

        cir.setReturnValue(chunk);
    }

}
