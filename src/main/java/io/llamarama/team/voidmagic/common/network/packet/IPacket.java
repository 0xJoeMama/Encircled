package io.llamarama.team.voidmagic.common.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket {

    void encode(IPacket packet, PacketBuffer buffer);

    <PCT extends IPacket> PCT decode(PacketBuffer buffer);

    boolean handle(Supplier<NetworkEvent.Context> contextSupplier);

}
