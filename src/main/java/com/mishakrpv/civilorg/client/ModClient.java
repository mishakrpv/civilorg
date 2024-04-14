package com.mishakrpv.civilorg.client;

import com.mishakrpv.civilorg.CivilOrg;
import com.mishakrpv.civilorg.player.data.PlayerData;
import com.mishakrpv.civilorg.player.data.enums.ClassName;
import com.mishakrpv.civilorg.player.gamerule.BowGameRule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

public class ModClient implements ClientModInitializer {
    public static PlayerData playerData = new PlayerData();
    public static BowGameRule bowGameRule = new BowGameRule();

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(CivilOrg.CLASS_CHANGED, (client, handler, buf, responseSender) -> {
            String className = buf.readString();

            bowGameRule.setClass(ClassName.byName(className, null));

            client.execute(() -> {
                client.player.sendMessage(Text.literal("Player specific class name: " + className));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(CivilOrg.INITIAL_SYNC, (client, handler, buf, responseSender) -> {
            playerData.className = buf.readString();

            bowGameRule.setClass(ClassName.byName(playerData.className, null));

            client.execute(() -> {
                client.player.sendMessage(Text.literal("Initial specific class name: " + playerData.className));
            });
        });
    }
}