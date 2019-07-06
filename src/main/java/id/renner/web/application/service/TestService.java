package id.renner.web.application.service;

import id.renner.web.library.injection.Inject;

import java.time.LocalDateTime;

@Inject
public class TestService {

    public String getTimeString() {
        return "the time is: " + LocalDateTime.now();
    }
}