package dev.vertcode.vcore.parsers;

import dev.vertcode.vcore.object.IStringParsable;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;


public class DoubleParsable implements IStringParsable<Double> {

    @Override
    public @Nullable Double getValue(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("1.0", "2.0", "3.0", "4.0", "5.0");
    }

    @Override
    public Class<Double> getParsableClass() {
        return Double.class;
    }
}
