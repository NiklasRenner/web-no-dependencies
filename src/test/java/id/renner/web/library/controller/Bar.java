package id.renner.web.library.controller;

import id.renner.web.library.injection.Inject;

@Inject
public class Bar {
    private final Baz baz;

    public Bar(Baz baz) {
        this.baz = baz;
    }

    public String bar() {
        return "bar " + baz.baz();
    }
}