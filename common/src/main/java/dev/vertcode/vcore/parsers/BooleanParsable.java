package dev.vertcode.vcore.parsers;

import dev.vertcode.vcore.object.IStringParsable;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class BooleanParsable implements IStringParsable<Boolean> {
    @Override
    public @Nullable Boolean getValue(String string) {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("yes") ||
                string.equalsIgnoreCase("y");
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("true", "false");
    }

    @Override
    public Class<Boolean> getParsableClass() {
        return Boolean.class;
    }

}
