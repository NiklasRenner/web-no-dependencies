package id.renner.web.library.json;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversionHandler {
    private Map<ConversionKey, Converter> converters;
    private boolean prettyPrint;

    public ConversionHandler(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        this.converters = new HashMap<>();

        this.converters.put(new ConversionKey(String.class, Boolean.class), (Converter<String, Boolean>) Boolean::parseBoolean);
        this.converters.put(new ConversionKey(String.class, boolean.class), (Converter<String, Boolean>) Boolean::parseBoolean);
        this.converters.put(new ConversionKey(String.class, Integer.class), (Converter<String, Integer>) Integer::parseInt);
        this.converters.put(new ConversionKey(String.class, int.class), (Converter<String, Integer>) Integer::parseInt);
        this.converters.put(new ConversionKey(String.class, Long.class), (Converter<String, Long>) Long::parseLong);
        this.converters.put(new ConversionKey(String.class, long.class), (Converter<String, Long>) Long::parseLong);

        this.converters.put(new ConversionKey(Boolean.class, String.class), (Converter<Boolean, String>) (input) -> Boolean.toString(input));
        this.converters.put(new ConversionKey(boolean.class, String.class), (Converter<Boolean, String>) (input) -> Boolean.toString(input));
        this.converters.put(new ConversionKey(Integer.class, String.class), (Converter<Integer, String>) (input) -> Integer.toString(input));
        this.converters.put(new ConversionKey(int.class, String.class), (Converter<Integer, String>) (input) -> Integer.toString(input));
        this.converters.put(new ConversionKey(Long.class, String.class), (Converter<Long, String>) (input) -> Long.toString(input));
        this.converters.put(new ConversionKey(long.class, String.class), (Converter<Long, String>) (input) -> Long.toString(input));
    }

    public Converter createIfMissingAndGet(Class inputClass, Class outputClass) {
        if (inputClass != String.class && outputClass != String.class) {
            throw new RuntimeException("one class needs to be string");
        }

        if (inputClass == outputClass) {
            return (input) -> input;
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
        List<Triple<String, Method, Converter>> conversionBindings = new ArrayList<>();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get")) {
                String propertyName = name.substring(3).toLowerCase();
                if (!propertyName.equals("class")) {
                    Converter converter = createIfMissingAndGet(method.getReturnType(), String.class);
                    conversionBindings.add(Triple.of(propertyName, method, converter));
                }
            }
        }

        return (input) -> {
            JsonWriter jsonWriter = new JsonWriter(prettyPrint);

            jsonWriter.startElement();
            conversionBindings.forEach((binding) -> {
                try {
                    Object result = binding.getMiddle().invoke(input); // call get method
                    if (result != null) { // if property isnt set, skip it
                        jsonWriter.putProperty(binding.getLeft(), (String) binding.getRight().convert(result)); //TODO need to check type, since it matters
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

        return (T) converter.convert(object);
    }
}