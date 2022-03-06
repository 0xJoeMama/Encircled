package io.github.llamarama.team.voidmagic;

import io.github.llamarama.team.voidmagic.common.event.EventHandler;
import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import io.github.llamarama.team.voidmagic.common.register.*;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VoidMagic implements ModInitializer {

    public static final String MOD_ID = "voidmagic";
    private static final Logger LOGGER = LogManager.getLogger("VoidMagic");

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
