### web-no-dependencies
Attempt to create a web application with no dependencies, except ones for test.
Also no googling except when stuck for a long time and other stuff tried.

### googled
* How to get all classes in a given package 
* How to do inheritance for annotations
* How to configure java.util.Logger
* How to set default values for annotation
* How to use streams for maps
* How to close HttpServer properly(turns out you can't rebind socket to same address without setting option, 
which can't be done with HttpServer cause the implementation is package local. Therefor a new test setup is needed)
