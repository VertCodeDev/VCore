package dev.vertcode.vcore.parsers;

import dev.vertcode.vcore.object.IStringParsable;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;


public class IntegerParsable implements IStringParsable<Integer> {

    @Override
    public @Nullable Integer getValue(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("1", "2", "3", "4", "5");
    }

    @Override
    public Class<Integer> getParsableClass() {
        return Integer.class;
    }

}
