package id.renner.web.service;

import id.renner.web.injection.Inject;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Inject
public class InfoService {
    private static final Logger logger = Logger.getLogger(InfoService.class.getSimpleName());

    public InfoService() {
        logger.info("infoService started");
    }

    public String getInfo() {
        return "the time is: " + LocalDateTime.now();
    }
}