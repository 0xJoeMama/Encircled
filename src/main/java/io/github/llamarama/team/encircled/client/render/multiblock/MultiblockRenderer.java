package io.github.llamarama.team.encircled.client.render.multiblock;

import io.github.llamarama.team.encircled.api.multiblock.MultiblockRotation;
import io.github.llamarama.team.encircled.api.multiblock.MultiblockType;
import io.github.llamarama.team.encircled.common.lib.multiblock.predicates.BlockStatePredicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class MultiblockRenderer {

    public static void render(MultiblockType type, BlockPos center, MatrixStack matrices, VertexConsumerProvider vertices, int light, int overlay) {
        matrices.push();
        matrices.translate(-type.getSize().getX() / 2, -type.getSize().getY() / 2, -type.getSize().getZ() / 2);
        type.getKeysFor(MultiblockRotation.ZERO).forEach((pos, predicate) -> {
            if (predicate instanceof BlockStatePredicate blockStatePredicate) {
                matrices.push();
                matrices.translate(pos.getX(), pos.getY(), pos.getZ());
                MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                        blockStatePredicate.getClientRenderState(MinecraftClient.getInstance().world.getRandom()), matrices, vertices,
                        light,
                        overlay
                );
                matrices.pop();
            }
        });
        matrices.pop();
    }

}
