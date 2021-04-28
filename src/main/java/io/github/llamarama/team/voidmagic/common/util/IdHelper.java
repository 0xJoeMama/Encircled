package io.github.llamarama.team.voidmagic.common.util;

import io.github.llamarama.team.voidmagic.common.util.constants.ModConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public final class IdHelper {

    private IdHelper() {
    }

    public static String getNonNullPath(Block block) {
        ResourceLocation loc = block.getRegistryName();
        if (loc != null) {
            return loc.getPath();
        }

        return ModConstants.EMPTY;
    }

    public static String getNonNullPath(Item item) {
        ResourceLocation loc = item.getRegistryName();
        if (loc != null) {
            return loc.getPath();
        }

        return ModConstants.EMPTY;
    }

    public static String getIdString(Item item) {
        ResourceLocation registryName = item.getRegistryName();

        if (registryName == null)
            return ModConstants.EMPTY;

        return registryName.toString();
    }

    public static String getIdString(Block block) {
        ResourceLocation registryName = block.getRegistryName();

        if (registryName == null)
            return ModConstants.EMPTY;

        return registryName.toString();
    }

}
