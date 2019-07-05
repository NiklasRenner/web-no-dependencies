package id.renner.web.util;

import java.lang.annotation.Annotation;

public class AnnotationUtils {

    // TODO maybe exclude annotations like @Documented from search, as they are recursed through until max depth is hit for no reason
    // recursion done due to lack of inheritance for annotations. Instead annotations need to be annotated with the annotation they wanna inherit from, then this function will try to find them
    // stop recursion when 3 level deep, since annotations like @Documented annotate themselves, leading to stack overflow if not stopped
    private static boolean hasAnnotation(Class classToCheck, Class wantedAnnotation, int depth) {
        if (depth > 3) {
            return false;
        }

        Annotation[] classAnnotations = classToCheck.getAnnotations();
        for (Annotation classAnnotation : classAnnotations) {
            if (wantedAnnotation.isInstance(classAnnotation) || hasAnnotation(classAnnotation.annotationType(), wantedAnnotation, depth + 1)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasAnnotation(Class classToCheck, Class annotationToCheck) {
        return hasAnnotation(classToCheck, annotationToCheck, 0);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getAnnotation(Class classToCheck, Class<T> wantedAnnotation, int depth) {
        if (depth > 3) {
            return null;
        }

        Annotation[] classAnnotations = classToCheck.getAnnotations();
        for (Annotation classAnnotation : classAnnotations) {
            if (wantedAnnotation.isInstance(classAnnotation)) {
                return (T) classAnnotation;
            } else {
                T foundAnnotation = getAnnotation(classAnnotation.annotationType(), wantedAnnotation, depth + 1);
                if (foundAnnotation != null) {
                    return foundAnnotation;
                }
            }
        }

        return null;
    }

    public static <T> T getAnnotation(Class classToCheck, Class<T> wantedAnnotation) {
        return getAnnotation(classToCheck, wantedAnnotation, 0);
    }
}