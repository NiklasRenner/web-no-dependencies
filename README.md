# web-no-dependencies
Attempt to create a web application with no dependencies, except ones for test.
Also no googling except when stuck for a long time and other stuff tried.

## googled

##### How to get all classes in a given package
Involves looking through classpath/jar and then building qualified class names that can be understood by a classloader. See `ClassUtils`.

##### How to do inheritance for annotations 
Annotations don't support inheritance, so created `AnnotationUtils` that looks at annotations of annotations,
to create some kind of inheritance. Works as long as `AnnotationUtils` is used for everything related to annotations.

##### How to configure java.util.Logger
I wanted to change logging format, currently done with system property overrride.

#####  How to set default values for annotation
ex. `String packageName() default "foo.bar";`.

##### How to use streams for maps
ex. `entryStream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));`. See ControllerHandler for example.

##### How to close HttpServer properly
Had problem in test where only first test succeeded, cause next text would say "Address already in use", when creating `ApplicationCore`.
Turns out the problem is that a process can't stop listening on a port, and then bind to it again right after, without setting
"SO_REUSEADDR" property before binding a socket.
This sadly isn't an option for `com.sun.net.httpserver.HttpServer`, as the implementation is package local, and can't be changed
without hacks.
Since this was only a problem in test, `TestUtil` class was created to create new `ApplicationCore` instances, each with a unique port.

##### How to use new HttpClient
See `ControllerTest`.
