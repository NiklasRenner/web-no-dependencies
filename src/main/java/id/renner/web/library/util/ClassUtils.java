package id.renner.web.library.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;

public class ClassUtils {

    public static Set<Class> getClassesForPackage(String packageName) {
        Set<Class> classes = new HashSet<>();
        String packagePath = packageName.replace('.', '/');

        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                URLConnection connection = url.openConnection();

                if (connection instanceof JarURLConnection) {
                    classes.addAll(checkJarFile((JarURLConnection) connection, packageName, packagePath));
                } else {
                    classes.addAll(checkDirectory(url.getPath(), packageName));
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException("couldn't load classes in package " + packageName + ": " + ex.getMessage(), ex);
        }

        return classes;
    }

    private static Set<Class> checkDirectory(String path, String packageName) throws ClassNotFoundException {
        Set<Class> classes = new HashSet<>();

        File directory = new File(URLDecoder.decode(path, StandardCharsets.UTF_8));
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".class")) {
                        classes.add(Class.forName(getQualifiedClassNameFromFilePath(file.getPath(), packageName)));
                    } else if (file.isDirectory()) {
                        classes.addAll(checkDirectory(file.getPath(), packageName));
                    }
                }
            }
        }

        return classes;
    }

    private static Set<Class> checkJarFile(JarURLConnection connection, String packageName, String packagePath) throws ClassNotFoundException, IOException {
        Set<Class> classes = new HashSet<>();

        Enumeration<JarEntry> entries = connection.getJarFile().entries();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.startsWith(packagePath) && name.endsWith(".class")) { // need to check that stuff from other packages aren't loaded, as the whole jar containing the package will be loaded
                classes.add(Class.forName(getQualifiedClassNameFromFilePath(name, packageName)));
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