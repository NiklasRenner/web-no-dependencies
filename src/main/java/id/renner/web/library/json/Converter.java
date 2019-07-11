package id.renner.web.library.json;

import java.util.Map;

@FunctionalInterface
public interface Converter<I, O> {
    O convert(I input, Map<ConversionKey, Converter> context);
}