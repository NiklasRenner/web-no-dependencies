package id.renner.web.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ClassUtils {

    public static Set<Class> getClassesForPackage(String packageName) {
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

    private static String getQualifiedClassNameFromFilePath(String filePath, String packageName) {
        String qualifiedClassName = filePath.replaceAll("[\\\\/]", "."); // convert path dividers to dots

        return qualifiedClassName
                .substring(0, qualifiedClassName.length() - ".class".length()) // remove .class postfix
                .substring(qualifiedClassName.indexOf(packageName)); // remove path elements before package part
    }
}