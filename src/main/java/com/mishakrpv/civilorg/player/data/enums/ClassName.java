package com.mishakrpv.civilorg.player.data.enums;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public enum ClassName implements StringIdentifiable {
    NONE(0, "none"),
    FARMER(1, "farmer"),
    MINER(2, "miner"),
    SOLDIER(3, "soldier"),
    WARLOCK(4, "warlock"),
    FISHER(5, "fisher");

    public static final ClassName DEFAULT = NONE;
    public static final StringIdentifiable.EnumCodec<ClassName> CODEC = StringIdentifiable.createCodec(ClassName::values);
    private static final IntFunction<ClassName> BY_ID = ValueLists.createIdToValueFunction(ClassName::getId, values(), ValueLists.OutOfBoundsHandling.ZERO);
    private static final int UNKNOWN = -1;
    private final int id;
    private final String name;

    ClassName(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public static ClassName byId(int id) {
        return BY_ID.apply(id);
    }

    @Nullable
    @Contract("_,!null->!null;_,null->_")
    public static ClassName byName(String name, @Nullable ClassName defaultClassName) {
        ClassName className = CODEC.byId(name);
        return className != null ? className : defaultClassName;
    }

    public static int getId(@Nullable ClassName className) {
        return className != null ? className.id : UNKNOWN;
    }
}
