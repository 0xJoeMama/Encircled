package io.github.llamarama.team.encircled.common.util.misc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Util {

    @Contract(pure = true)
    public static boolean allTrue(boolean @NotNull ... conditions) {
        boolean ret = true;

        for (boolean cond: conditions) {
            ret = ret && cond;
        }

        return ret;
    }

}
