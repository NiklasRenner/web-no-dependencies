package id.renner.web.injection.error.config;

import id.renner.web.injection.Inject;

@Inject
public class Foo {
    private final Bar bar;

    public Foo(Bar bar) {
        this.bar = bar;
    }

    public Bar getBar() {
        return bar;
    }
}