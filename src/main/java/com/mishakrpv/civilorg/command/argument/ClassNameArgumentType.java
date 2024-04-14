package com.mishakrpv.civilorg.command.argument;

import com.mishakrpv.civilorg.player.data.enums.ClassName;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassNameArgumentType implements ArgumentType<ClassName> {
    private static final Collection<String> EXAMPLES;
    private static final ClassName[] VALUES;
    private static final DynamicCommandExceptionType INVALID_CLASS_NAME_EXCEPTION;

    public ClassNameArgumentType() {
    }

    @Override
    public ClassName parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readUnquotedString();
        ClassName className = ClassName.byName(string, null);
        if (className == null) {
            throw INVALID_CLASS_NAME_EXCEPTION.createWithContext(reader, string);
        } else {
            return className;
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof CommandSource ? CommandSource.suggestMatching(Arrays.stream(VALUES).map(ClassName::asString), builder) : Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static ClassNameArgumentType className() { return new ClassNameArgumentType(); }

    public static ClassName getClassName(CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
        return context.getArgument(name, ClassName.class);
    }

    static {
        EXAMPLES = Stream.of(ClassName.FARMER, ClassName.SOLDIER).map(ClassName::asString).collect(Collectors.toList());
        VALUES = ClassName.values();
        INVALID_CLASS_NAME_EXCEPTION = new DynamicCommandExceptionType((className) -> Text.literal("Unknown class name: " + className));
    }
}
