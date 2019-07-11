package id.renner.web.library.json;

import java.util.Objects;

public class ConversionKey {
    private final Class inputClass;
    private final Class outputClass;

    public ConversionKey(Class inputClass, Class outputClass) {
        this.inputClass = inputClass;
        this.outputClass = outputClass;
    }

    public Class getInputClass() {
        return inputClass;
    }

    public Class getOutputClass() {
        return outputClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionKey that = (ConversionKey) o;
        return Objects.equals(inputClass, that.inputClass) &&
                Objects.equals(outputClass, that.outputClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputClass, outputClass);
    }
}