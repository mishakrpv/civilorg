package com.mishakrpv.civilorg;

import com.mishakrpv.civilorg.command.ClassCommand;
import com.mishakrpv.civilorg.player.data.PlayerData;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CivilOrg implements ModInitializer {
    public static final String MOD_ID = "civilorg";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier CLASS_CHANGED = new Identifier(MOD_ID, "class_changed");
    public static final Identifier INITIAL_SYNC = new Identifier(MOD_ID, "initial_sync");

    @Override
    public void onInitialize() {
        ClassCommand.register();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerData playerState = StateSaverAndLoader.getPlayerState(handler.getPlayer());
            PacketByteBuf data = PacketByteBufs.create();
            data.writeString(playerState.className);
            server.execute(() -> {
                ServerPlayNetworking.send(handler.getPlayer(), INITIAL_SYNC, data);
            });
        });
    }
}