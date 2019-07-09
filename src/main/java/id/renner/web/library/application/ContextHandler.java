package id.renner.web.library.application;

import id.renner.web.library.injection.Inject;
import id.renner.web.library.injection.InjectionException;
import id.renner.web.library.util.AnnotationUtils;
import id.renner.web.library.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ContextHandler {
    private static final Logger logger = Logger.getLogger(ContextHandler.class.getSimpleName());

    private final Map<String, Object> injectedObjectContext;

    public ContextHandler(Class applicationClass) {
        logger.info("creating contextHandler");
        this.injectedObjectContext = new HashMap<>();

        final Application applicationAnnotation = AnnotationUtils.getAnnotation(applicationClass, Application.class);
        final String basePackage = applicationAnnotation.basePackage().isBlank() ? applicationClass.getPackageName() : applicationAnnotation.basePackage();
        final Set<Class> packageClasses = ClassUtils.getClassesForPackage(basePackage);

        packageClasses.stream()
                .filter(this::isInjectable)
                .forEach(this::getOrInjectInstance);
    }

    private boolean isInjectable(Class clazz) {
        return AnnotationUtils.hasAnnotation(clazz, Inject.class) && !clazz.isAnnotation();
    }

    private Object getOrInjectInstance(Class clazz) {
        if (injectedObjectContext.containsKey(clazz.getCanonicalName())) {
            return injectedObjectContext.get(clazz.getCanonicalName());
        }

        try {
            Object instance = injectInstance(clazz);
            injectedObjectContext.put(clazz.getCanonicalName(), instance);
            return instance;
        } catch (StackOverflowError ex) {
            throw new InjectionException("circular dependencies caused stack-overflow");
        }
    }

    private Object injectInstance(Class clazz) {
        Constructor[] constructors = clazz.getConstructors();
        if (constructors.length > 1) {
            throw new InjectionException("can't inject class [" + clazz.getCanonicalName() + "], multiple injectable constructors present");
        }

        Constructor constructor = constructors[0];
        Object[] params = getOrInjectParams(clazz, constructor);

        try {
            logger.info("creating instance of [" + clazz.getCanonicalName() + "]");
            return constructor.newInstance(params);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("error creating instance of class [" + clazz.getCanonicalName() + "]: " + ex.getMessage(), ex);
        }
    }

    private Object[] getOrInjectParams(Class clazz, Constructor constructor) {
        Class[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class paramType = paramTypes[i];
            if (!isInjectable(paramType)) {
                throw new InjectionException("found class [" + paramType.getCanonicalName() + "] not marked with @Inject, while trying to create instance of [" + clazz.getCanonicalName() + "]");
            }

            Object instance = getOrInjectInstance(paramType); // recursion to avoid having to create everything in the right order
            params[i] = instance;
        }

        return params;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz) {
        return (T) injectedObjectContext.get(clazz.getCanonicalName());
    }

    public Map<String, Object> getInjectedObjectContext() {
        return injectedObjectContext;
    }
}