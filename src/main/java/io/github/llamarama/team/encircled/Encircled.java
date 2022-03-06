package io.github.llamarama.team.encircled;

import io.github.llamarama.team.encircled.common.event.EventHandler;
import io.github.llamarama.team.encircled.common.network.ModNetworking;
import io.github.llamarama.team.encircled.common.register.*;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Encircled implements ModInitializer {

    public static final String MOD_ID = "encircled";
    private static final Logger LOGGER = LogManager.getLogger("Encircled");

    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void onInitialize() {
        // TODO Look into fabric cloth config api.
        // ConfigInitializer.init(ModLoadingContext.get());

        ModBlocks.init();
        ModItems.init();
        ModBlockEntityTypes.init();
        ModMultiblocks.init();
        ModNetworking.get().init();
        EventHandler.register();
    }

}
