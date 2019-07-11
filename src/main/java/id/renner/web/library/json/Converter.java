package id.renner.web.library.json;

@FunctionalInterface
public interface Converter<I, O> {
    O convert(I input);
}