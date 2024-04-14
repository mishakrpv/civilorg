package com.mishakrpv.civilorg.command;

import com.mishakrpv.civilorg.StateSaverAndLoader;
import com.mishakrpv.civilorg.command.argument.ClassNameArgumentType;
import com.mishakrpv.civilorg.player.data.PlayerData;
import com.mishakrpv.civilorg.player.data.enums.ClassName;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static com.mishakrpv.civilorg.CivilOrg.CLASS_CHANGED;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;


public class ClassCommand {
    public static final int REQUIRED_PERMISSION_LEVEL = 0;

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("class")
            .requires(source -> source.hasPermissionLevel(REQUIRED_PERMISSION_LEVEL))
            .then(argument("classname", ClassNameArgumentType.className())
                .executes(context -> {
                    return execute(context, Collections.singleton((context.getSource()).getPlayerOrThrow()), ClassNameArgumentType.getClassName(context, "classname"));
                })
                .then(argument("target", EntityArgumentType.players()).executes((context -> {
                    return execute(context, EntityArgumentType.getPlayers(context, "target"), ClassNameArgumentType.getClassName(context, "classname"));
                }))))));
    }

    private static void sendFeedback(ServerCommandSource source, ServerPlayerEntity playerEntity, ClassName className) {
        if (source.getEntity() == playerEntity) {
            source.sendFeedback(() -> {
                return Text.literal("Set own class to " + className.asString());
            }, true);
        } else {
            if (source.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK)) {
                playerEntity.sendMessage(Text.literal("Your class has been updated to " + className.asString()));
            }

            source.sendFeedback(() -> {
                return Text.literal("Set " + playerEntity.getDisplayName() + "'s class to " + className.asString());
            }, true);
        }
    }

    private static int execute(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets, ClassName className) {
        int i = 0;
        Iterator iterator = targets.iterator();

        while(iterator.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)iterator.next();

            PlayerData playerState = StateSaverAndLoader.getPlayerState(serverPlayerEntity);
            playerState.className = className.asString();

            MinecraftServer server = context.getSource().getServer();
            PacketByteBuf data = PacketByteBufs.create();
            data.writeString(playerState.className);

            server.execute(() -> {
                ServerPlayNetworking.send(serverPlayerEntity, CLASS_CHANGED, data);
            });

            sendFeedback(context.getSource(), serverPlayerEntity, className);
            ++i;
        }

        return i;
    }
}
