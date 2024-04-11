package com.mishakrpv.civilorg;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Initializer implements ModInitializer {
    public static final String MOD_ID = "civilorg";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Test log message!");
    }
}