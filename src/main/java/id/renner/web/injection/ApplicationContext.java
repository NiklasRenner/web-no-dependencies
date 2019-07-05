package id.renner.web.injection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
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
        Application applicationAnnotation = (Application) applicationClass.getAnnotation(Application.class);
        getClassesForPackage(applicationAnnotation.basePackage()).stream()
                .filter(this::isMarkedToBeInjected)
                .forEach(this::getOrCreateInstance);
    }

    private Set<Class> getClassesForPackage(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Set<Class> classes = new HashSet<>();

        String packagePath = packageName.replace(".", "/");
        Enumeration<URL> packages;
        try {
            packages = classLoader.getResources(packagePath);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        ArrayList<File> directories = new ArrayList<>();
        while (packages.hasMoreElements()) {
            directories.add(new File(URLDecoder.decode(packages.nextElement().getPath(), StandardCharsets.UTF_8)));
        }

        while (!directories.isEmpty()) {
            File directory = directories.remove(0);

            if (directory.exists()) {
                File[] files = directory.listFiles();
                if (files == null) {
                    continue;
                }

                for (File file : files) {
                    if (file.getName().endsWith(".class") && (!file.getName().contains("$"))) { // only interested in top level classes
                        try {
                            classes.add(Class.forName(getQualifiedClassNameFromFilePath(file.getPath(), packageName)));
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else if (file.isDirectory()) {
                        directories.add(file);
                    }
                }
            }
        }

        return classes;
    }

    private String getQualifiedClassNameFromFilePath(String filePath, String packageName) {
        String qualifiedClassName = filePath.replaceAll("[\\\\/]", "."); // convert path dividers to dots

        return qualifiedClassName
                .substring(0, qualifiedClassName.length() - ".class".length()) // remove .class postfix
                .substring(qualifiedClassName.indexOf(packageName)); // remove path elements before package part
    }

    private boolean isMarkedToBeInjected(Class clazz) {
        return clazz.getAnnotation(Inject.class) != null;
    }

    private Object getOrCreateInstance(Class clazz) {
        if (objectInstances.containsKey(clazz.getCanonicalName())) { // only create instances if they don't already exist and are marked with @Inject
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
            logger.info("trying to create instance of: " + clazz.getCanonicalName());
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
            if (!isMarkedToBeInjected(paramType)) {
                throw new InjectionException("found class " + paramType.getCanonicalName() + " not marked with @Inject, while trying to create instance of " + clazz.getCanonicalName());
            }
            Object instance = getOrCreateInstance(paramType); // recursion to avoid having to checkClassForInjectAndHandle everything in the right order

            params[i] = instance;
        }
        return params;
    }

    public <T> T getInstance(Class<T> clazz) {
        return (T) objectInstances.get(clazz.getCanonicalName());
    }
}