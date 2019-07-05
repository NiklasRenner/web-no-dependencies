package id.renner.web.injection;

import id.renner.web.util.AnnotationUtils;
import id.renner.web.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ApplicationContext {
    private static final Logger logger = Logger.getLogger(ApplicationContext.class.getSimpleName());

    private final Map<String, Object> objectInstances = new HashMap<>();
    private final Class applicationClass;

    public ApplicationContext(Class applicationClass) {
        this.applicationClass = applicationClass;
        init();
    }

    private void init() {
        // TODO fix logging setup, this currently needs to run before the first log statement of the application, else it doesn't apply
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s() %5$s%6$s%n");

        Application applicationAnnotation = AnnotationUtils.getAnnotation(applicationClass, Application.class);
        Set<Class> packageClasses = ClassUtils.getClassesForPackage(applicationAnnotation.basePackage());

        // dependency injection setup
        packageClasses.stream()
                .filter(this::isInjectable)
                .forEach(this::getOrCreateInstance);
    }

    private boolean isInjectable(Class clazz) {
        return AnnotationUtils.hasAnnotation(clazz, Inject.class) && !clazz.isAnnotation();
    }

    private Object getOrCreateInstance(Class clazz) {
        if (objectInstances.containsKey(clazz.getCanonicalName())) {
            return objectInstances.get(clazz.getCanonicalName());
        }

        Object instance = createInstance(clazz);
        objectInstances.put(clazz.getCanonicalName(), instance);
        return instance;
    }

    private Object createInstance(Class clazz) {
        Constructor[] constructors = clazz.getConstructors();
        if (constructors.length > 1) {
            throw new InjectionException("can't inject class " + clazz.getCanonicalName() + ", multiple injectable constructors present");
        }

        Constructor constructor = constructors[0];
        Object[] params = getOrCreateParams(clazz, constructor);

        try {
            logger.info("creating instance of: " + clazz.getCanonicalName());
            return constructor.newInstance(params);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("error creating instance of class + " + clazz.getCanonicalName() + ": " + ex.getMessage(), ex);
        }
    }

    private Object[] getOrCreateParams(Class clazz, Constructor constructor) {
        Class[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class paramType = paramTypes[i];
            if (!isInjectable(paramType)) {
                throw new InjectionException("found class " + paramType.getCanonicalName() + " not marked with @Inject, while trying to create instance of " + clazz.getCanonicalName());
            }

            Object instance = getOrCreateInstance(paramType); // recursion to avoid having to create everything in the right order
            params[i] = instance;
        }

        return params;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz) {
        return (T) objectInstances.get(clazz.getCanonicalName());
    }
}