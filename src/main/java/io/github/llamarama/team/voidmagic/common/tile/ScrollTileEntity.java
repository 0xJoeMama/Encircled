package io.github.llamarama.team.voidmagic.common.tile;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.voidmagic.api.spellbinding.ICircleCaster;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.CircleMultiblock;
import io.github.llamarama.team.voidmagic.common.lib.spellbinding.CircleRegistry;
import io.github.llamarama.team.voidmagic.common.register.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ScrollTileEntity extends TileEntity implements ITickableTileEntity, ICircleCaster {

    private int craftingTick;
    private ISpellbindingCircle currentCircle;

    public ScrollTileEntity() {
        super(ModTileEntityTypes.SCROLL.get());
        this.craftingTick = 0;
    }

    @Override
    public void tick() {
        if (this.world == null)
            return;

        //noinspection StatementWithEmptyBody
        if (world.isRemote) {
            /*
               Maybe client logic.
             */
        }

            /*
                Start the server logic.
             */
        // Check for correct position.
        BlockState currentState = this.getBlockState();
        if (currentState.get(ModBlockProperties.OPEN) && !currentState.isValidPosition(this.world, this.getPos()))
            this.world.destroyBlock(this.getPos(), true);

        // Circle logic.
        if (!this.isCrafting() && this.currentCircle == null) {
            this.validateCurrentCircle();
        } else if (this.craftingTick == 0) {
            this.finishCrafting();
        } else if (this.currentCircle.multiblock().existsAt(this.pos, this.world)) {
            this.progressCrafting();
        }
    }

    private void validateCurrentCircle() {
        Set<ISpellbindingCircle> possibleCircles = CircleRegistry.getRegistry().keySet().stream()
                .filter(iSpellbindingCircle -> iSpellbindingCircle.multiblock().existsAt(this.pos, this.world))
                .collect(Collectors.toSet());

        Optional<ISpellbindingCircle> finalType = possibleCircles.stream()
                .reduce((circle1, circle2) -> {
                    Vector3i size1 = circle1.multiblock().getSize();
                    Vector3i size2 = circle2.multiblock().getSize();

                    boolean isXLarger = size1.getX() > size2.getX();
                    boolean isYLarger = size1.getY() > size2.getY();
                    boolean isZLarger = size1.getZ() > size2.getZ();

                    return isXLarger && isYLarger && isZLarger ? circle1 :
                            !isXLarger && !isYLarger && !isZLarger ? circle2 : circle1;
                });

        finalType.ifPresent(this::initiateCircle);
        finalType.ifPresent(VoidMagic.getLogger()::debug);
        VoidMagic.getLogger().debug("I found smth...");
    }

    private void initiateCircle(ISpellbindingCircle circle) {
        this.craftingTick = circle.getCraftingTime();
        this.setCircle(circle);
    }

    private void finishCrafting() {
        this.currentCircle.getResult()
                .circleFormed(this.world, this.pos, this.getBlockState(),
                        new CircleMultiblock(this.currentCircle.multiblock(), this.pos, this.world));
        this.currentCircle = null;
    }

    private void progressCrafting() {
        this.craftingTick--;
        VoidMagic.getLogger().debug("Tick");
    }

    private boolean isCrafting() {
        return this.craftingTick > 0;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        Optional.ofNullable(this.world).ifPresent((world) -> {
            CompoundNBT tag = pkt.getNbtCompound();
            BlockPos pos = pkt.getPos();

            this.handleUpdateTag(world.getBlockState(pos), tag);
        });
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.getPos().add(-1, 0, -1), this.getPos().add(1, 1, 1));
    }

    @Override
    public ISpellbindingCircle getCircle() {
        return this.currentCircle;
    }

    @Override
    public void setCircle(ISpellbindingCircle circle) {
        this.currentCircle = circle;
    }

}
