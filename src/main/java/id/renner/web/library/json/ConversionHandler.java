package id.renner.web.library.json;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversionHandler {
    private Map<ConversionKey, Converter> converters;
    private JsonWriter jsonWriter;

    public ConversionHandler() {
        this.jsonWriter = new JsonWriter(true);
        this.converters = new HashMap<>();

        this.converters.put(new ConversionKey(String.class, Boolean.class), (Converter<String, Boolean>) (input, context) -> Boolean.parseBoolean(input));
        this.converters.put(new ConversionKey(String.class, boolean.class), (Converter<String, Boolean>) (input, context) -> Boolean.parseBoolean(input));
        this.converters.put(new ConversionKey(String.class, Integer.class), (Converter<String, Integer>) (input, context) -> Integer.parseInt(input));
        this.converters.put(new ConversionKey(String.class, int.class), (Converter<String, Integer>) (input, context) -> Integer.parseInt(input));
        this.converters.put(new ConversionKey(String.class, Long.class), (Converter<String, Long>) (input, context) -> Long.parseLong(input));
        this.converters.put(new ConversionKey(String.class, long.class), (Converter<String, Long>) (input, context) -> Long.parseLong(input));

        this.converters.put(new ConversionKey(Boolean.class, String.class), (Converter<Boolean, String>) (input, context) -> Boolean.toString(input));
        this.converters.put(new ConversionKey(boolean.class, String.class), (Converter<Boolean, String>) (input, context) -> Boolean.toString(input));
        this.converters.put(new ConversionKey(Integer.class, String.class), (Converter<Integer, String>) (input, context) -> Integer.toString(input));
        this.converters.put(new ConversionKey(int.class, String.class), (Converter<Integer, String>) (input, context) -> Integer.toString(input));
        this.converters.put(new ConversionKey(Long.class, String.class), (Converter<Long, String>) (input, context) -> Long.toString(input));
        this.converters.put(new ConversionKey(long.class, String.class), (Converter<Long, String>) (input, context) -> Long.toString(input));
    }

    public Converter createIfMissingAndGet(Class inputClass, Class outputClass) {
        if (inputClass != String.class && outputClass != String.class) {
            throw new RuntimeException("one class needs to be string");
        }

        if (inputClass == outputClass) {
            return (input, context) -> input;
        }

        ConversionKey conversionKey = new ConversionKey(inputClass, outputClass);
        if (converters.containsKey(conversionKey)) {
            return converters.get(conversionKey);
        }

        if (inputClass == String.class) {
            return createStringToClassConverter(outputClass);
        } else {
            Converter classToStringConverter = createClassToStringConverter(inputClass);
            converters.put(conversionKey, classToStringConverter);
            return classToStringConverter;
        }
    }

    private Converter createStringToClassConverter(Class outputClass) {
        //TODO
        return null;
    }

    private Converter createClassToStringConverter(Class clazz) {
        Method[] methods = clazz.getMethods();
        List<Pair<String, Method>> bindings = new ArrayList<>();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get")) {
                String propertyName = name.substring(3).toLowerCase();
                if (!propertyName.equals("class")) {
                    bindings.add(Pair.of(propertyName, method));
                }
            }
        }

        List<Triple<String, Method, Converter>> conversionBindings = new ArrayList<>();
        bindings.forEach((binding) -> {
            Converter converter = createIfMissingAndGet(binding.getRight().getReturnType(), String.class);
            conversionBindings.add(Triple.of(binding.getLeft(), binding.getRight(), converter));
        });

        return (input, context) -> {
            JsonWriter jsonWriter = new JsonWriter(true);

            jsonWriter.startElement();
            conversionBindings.forEach((binding) -> {
                try {
                    Object result = binding.getMiddle().invoke(input);
                    if (result != null) { // if property isnt set, skip it
                        jsonWriter.putProperty(binding.getLeft(), (String) binding.getRight().convert(result, context));
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            jsonWriter.endElement();

            return jsonWriter.getString();
        };
    }

    public <T> T convert(Object object, Class<T> outputClass) {
        if (object.getClass() == outputClass) {
            return (T) object;
        }

        ConversionKey conversionKey = new ConversionKey(object.getClass(), outputClass);
        Converter converter = converters.get(conversionKey);
        if (converter == null) {
            throw new RuntimeException("no converter found for class [" + object.getClass().getSimpleName() + "]");
        }

        return (T) converter.convert(object, converters);
    }
}