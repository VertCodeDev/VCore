package dev.vertcode.vcore.parsers;

import dev.vertcode.vcore.object.IStringParsable;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;


public class FloatParsable implements IStringParsable<Float> {

    @Override
    public @Nullable Float getValue(String string) {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("1.0", "2.0", "3.0", "4.0", "5.0");
    }

    @Override
    public Class<Float> getParsableClass() {
        return Float.class;
    }

}
