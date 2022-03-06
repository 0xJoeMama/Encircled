package io.github.llamarama.team.encircled.common.util;

import io.github.llamarama.team.encircled.Encircled;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public interface IdBuilder {

    @NotNull
    static Identifier of(String id) {
        return new Identifier(Encircled.MOD_ID, id);
    }

}
