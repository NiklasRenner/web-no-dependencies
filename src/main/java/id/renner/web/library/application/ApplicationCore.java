package id.renner.web.library.application;

import java.util.logging.Logger;

public class ApplicationCore {
    private static final Logger logger = Logger.getLogger(ApplicationCore.class.getSimpleName());

    private final ContextHandler contextHandler;
    private final ControllerHandler controllerHandler;

    public ApplicationCore(Class applicationClass, int port) {
        // TODO fix logging setup, this currently needs to run before the first log statement of the application, else it doesn't apply
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s() %5$s%6$s%n");

        logger.info("starting applicationCore");
        this.contextHandler = new ContextHandler(applicationClass);
        this.controllerHandler = new ControllerHandler(contextHandler, port);
    }

    public ApplicationCore(Class applicationClass) {
        this(applicationClass, 8080);
    }

    public ContextHandler getContextHandler() {
        return contextHandler;
    }

    public ControllerHandler getControllerHandler() {
        return controllerHandler;
    }

    public void stop() {
        controllerHandler.stop();
    }
}