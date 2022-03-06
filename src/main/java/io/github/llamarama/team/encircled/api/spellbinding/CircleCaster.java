package io.github.llamarama.team.encircled.api.spellbinding;

import io.github.llamarama.team.encircled.api.multiblock.MultiblockType;

public interface CircleCaster {

    SpellbindingCircle getCircle();

    default MultiblockType multiblockType() {
        return this.getCircle().getMultiblockType();
    }

}
