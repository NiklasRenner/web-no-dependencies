package id.renner.web.library.injection.error.config;

import id.renner.web.library.injection.Inject;

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