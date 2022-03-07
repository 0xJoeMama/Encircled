package io.github.llamarama.team.encircled.common.block_entity;

import io.github.llamarama.team.encircled.api.tile.OfferingPlateInventory;
import io.github.llamarama.team.encircled.common.register.ModBlockEntityTypes;
import io.github.llamarama.team.encircled.common.util.InventoryUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OfferingPlateBlockEntity extends BlockEntity implements OfferingPlateInventory {

    private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
    @Environment(EnvType.CLIENT)
    public float rotationTick;

    public OfferingPlateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.OFFERING_PLATE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getStacks() {
        return this.stacks;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        InventoryUtils.writeInventory(nbt, this.stacks);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        InventoryUtils.readInventory(nbt, this.stacks);
    }

    public void interact(@NotNull PlayerEntity player) {
        if (!player.world.isClient){
            ItemStack playerStack = player.getStackInHand(player.getActiveHand());
            ItemStack stackInHand = playerStack.copy();
            ItemStack stackInSlot = this.removeStack(0);

            if (stackInSlot.isEmpty()) {
                stackInHand.setCount(1);
                playerStack.decrement(1);
                this.setStack(0, stackInHand);
            } else {
                if (!player.giveItemStack(stackInSlot)) {
                    Vec3d pos = Vec3d.ofCenter(this.getPos()).add(0.5, 0.5, 0.5);
                    player.getWorld().spawnEntity(new ItemEntity(player.world, pos.x, pos.y, pos.z, stackInSlot));
                }
            }

            // At the moment we yeet a random packet to the player. It may be better to use a custom one but the
            // mojang one seems to work for now so ig its staying lol
            ((ServerPlayerEntity) player).networkHandler.sendPacket(this.toUpdatePacket());
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

}
