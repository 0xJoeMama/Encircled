package io.github.llamarama.team.encircled.common.register;

import io.github.llamarama.team.encircled.api.multiblock.MultiblockType;
import io.github.llamarama.team.encircled.common.lib.multiblock.DefaultPredicates;
import io.github.llamarama.team.encircled.common.lib.multiblock.impl.DefaultMultiblockType;
import io.github.llamarama.team.encircled.common.util.IdBuilder;
import net.minecraft.block.Blocks;

@SuppressWarnings("unused")
public final class ModMultiblocks {

    public static final MultiblockType RANDOM_TYPE =
            DefaultMultiblockType.Builder.create(IdBuilder.of("random"),
                            5, 1, 5, false)
                    .define('P', DefaultPredicates.match(Blocks.WHITE_WOOL))
                    .pattern(new String[][]{
                            {
                                    "PPPPP",
                                    "PPPPP",
                                    "PPPPP",
                                    "PPPPP",
                                    "PPPPP"
                            }
                    }).build();

    private ModMultiblocks() {

    }

    public static void init() {

    }

}
