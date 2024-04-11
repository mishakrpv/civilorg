package com.mishakrpv.civilorg.persistent;

import com.mishakrpv.civilorg.Initializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.UUID;

public class ServerState extends PersistentState {
    public HashMap<UUID, PlayerData> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        return null;
    }

    public static ServerState createFromNbt(NbtCompound tag) {
        ServerState state = new ServerState();
        return state;
    }

    private static Type<ServerState> type = new Type<>(
        ServerState::new,
        ServerState::createFromNbt,
        null
    );

    public static ServerState getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        ServerState state = persistentStateManager.getOrCreate(type, Initializer.MOD_ID);
        state.markDirty();
        return state;
    }

    public static PlayerData getPlayerState(LivingEntity player) {
        ServerState serverState = getServerState(player.getWorld().getServer());
        PlayerData playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());
        return playerState;
    }

}
